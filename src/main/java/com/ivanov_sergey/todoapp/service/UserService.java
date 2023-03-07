package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.persist.model.User;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;

public interface UserService {
    User registerNewUserAccount(SignupRequest signupRequest);
//    User registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException;

    void updateRegisteredUser(User user);
}
