package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.model.VerificationToken;
import com.ivanov_sergey.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
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
    @GetMapping("/registration")
    public ResponseEntity<HttpStatus> confirmRegistration(@RequestParam("token") String token) {

        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (tokenIsExpired(verificationToken)) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private boolean tokenIsExpired(VerificationToken verificationToken){
        Calendar cal = Calendar.getInstance();
        return (verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0;
    }
}
