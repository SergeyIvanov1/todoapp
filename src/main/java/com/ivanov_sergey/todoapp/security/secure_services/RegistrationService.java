package com.ivanov_sergey.todoapp.security.secure_services;

import com.ivanov_sergey.todoapp.persist.model.Role;
import com.ivanov_sergey.todoapp.persist.model.User;
import com.ivanov_sergey.todoapp.persist.model.VerificationToken;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;

import java.util.Set;


public interface RegistrationService {
    void addToDBVerificationToken(com.ivanov_sergey.todoapp.persist.model.User user, String token);

    VerificationToken getVerificationToken(String token);
    User getUser(String verificationToken);
    Set<Role> getRolesFromRequest(SignupRequest signUpRequest);
}
