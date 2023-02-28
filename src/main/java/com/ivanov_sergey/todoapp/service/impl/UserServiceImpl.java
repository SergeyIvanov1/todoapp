package com.ivanov_sergey.todoapp.service.impl;

import com.ivanov_sergey.todoapp.enums.ERole;
import com.ivanov_sergey.todoapp.exception_handling.UserAlreadyExistException;
import com.ivanov_sergey.todoapp.model.Role;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.repository.UserRepository;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;
import com.ivanov_sergey.todoapp.service.RegistrationService;
import com.ivanov_sergey.todoapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final RegistrationService registrationService;
    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public UserServiceImpl(RegistrationService registrationService,
                           UserRepository userRepository,
                           PasswordEncoder encoder) {
        this.registrationService = registrationService;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public User registerNewUserAccount(SignupRequest signupRequest) throws UserAlreadyExistException{
        return userRepository.save(User.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(encoder.encode(signupRequest.getPassword()))
                .roles(registrationService.getRolesFromRequest(signupRequest))
                .build());
    }

//    public UserDetails loadUserByUsername(String email)
//            throws UsernameNotFoundException {
//
//        boolean enabled = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
//        try {
//            com.ivanov_sergey.todoapp.model.User user = userRepository.findByEmail(email);
//            if (user == null) {
//                throw new UsernameNotFoundException(
//                        "No user found with username: " + email);
//            }
//
//            return new org.springframework.security.core.userdetails.User(
//                    user.getEmail(),
//                    user.getPassword().toLowerCase(),
//                    user.isEnabled(),
//                    accountNonExpired,
//                    credentialsNonExpired,
//                    accountNonLocked,
////                    getAuthorities(user.getRole()));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public void updateRegisteredUser(User user) {
        userRepository.save(user);
    }
}
