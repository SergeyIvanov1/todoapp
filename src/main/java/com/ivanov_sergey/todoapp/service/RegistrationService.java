package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.model.Role;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.model.VerificationToken;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;
import org.springframework.stereotype.Service;

import java.util.Set;


public interface RegistrationService {
    void addToDBVerificationToken(com.ivanov_sergey.todoapp.model.User user, String token);

    VerificationToken getVerificationToken(String token);
    User getUser(String verificationToken);
    Set<Role> getRolesFromRequest(SignupRequest signUpRequest);
}
