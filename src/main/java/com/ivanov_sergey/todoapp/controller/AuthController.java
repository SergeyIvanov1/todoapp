package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.exception_handling.UserAlreadyExistException;
import com.ivanov_sergey.todoapp.model.Role;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.model.VerificationToken;
import com.ivanov_sergey.todoapp.repository.UserRepository;
import com.ivanov_sergey.todoapp.security.email_registration.events.OnRegistrationCompleteEvent;
import com.ivanov_sergey.todoapp.security.jwt.JwtUtils;
import com.ivanov_sergey.todoapp.security.payload.request.LoginRequest;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;
import com.ivanov_sergey.todoapp.security.payload.request.TokenRequest;
import com.ivanov_sergey.todoapp.security.payload.response.JwtResponse;
import com.ivanov_sergey.todoapp.security.payload.response.MessageResponse;
import com.ivanov_sergey.todoapp.security.services.UserDetailsImpl;
import com.ivanov_sergey.todoapp.service.RegistrationService;
import com.ivanov_sergey.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final ApplicationEventPublisher eventPublisher;
    private final UserService userService;
    private final RegistrationService registrationService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          PasswordEncoder encoder,
                          JwtUtils jwtUtils,
                          ApplicationEventPublisher eventPublisher,
                          UserService userService,
                          RegistrationService registrationService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.eventPublisher = eventPublisher;
        this.userService = userService;
        this.registrationService = registrationService;
    }

    @Operation(summary = "Register an account of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignupRequest.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = @Content)
    })
    @PostMapping("/users/signup")
    public ResponseEntity<HttpStatus> registerUserAccount(@Valid @RequestBody SignupRequest signupRequest,
                                                          HttpServletRequest request) {

        if(emailIsExists(signupRequest.getEmail())){
            throw new UserAlreadyExistException("There is an account with that email address: " + signupRequest.getEmail());
        }
        User registeredUser = userService.registerNewUserAccount(signupRequest);
        String appUrl = request.getContextPath();

        // sending an email to user
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser, request.getLocale(), appUrl));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Confirmation of user registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Accepted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Token Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "423", description = "Token Locked",
                    content = @Content)
    })
    @PostMapping("/confirm_email")
    public ResponseEntity<HttpStatus> confirmRegistration(@RequestBody TokenRequest tokenRequest) {
//    public ResponseEntity<HttpStatus> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = registrationService.getVerificationToken(tokenRequest.getToken());
        if (verificationToken == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (tokenIsExpired(verificationToken)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);
        userService.updateRegisteredUser(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/signin")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        JwtResponse jwtResponse = new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
        System.out.println(jwtResponse);
        return ResponseEntity.ok(jwtResponse);
    }

    // it is endpoint for registration admins and moderators. Not tested
    @PostMapping("/admin/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                             signUpRequest.getEmail(),
                             encoder.encode(signUpRequest.getPassword()));

        Set<Role> roles = registrationService.getRolesFromRequest(signUpRequest);
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    private boolean tokenIsExpired(VerificationToken verificationToken){
        Calendar cal = Calendar.getInstance();
        return (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }

    private boolean emailIsExists(String email){
        return userRepository.findByEmail(email) != null;
    }
}
