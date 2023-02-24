package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.dto.UserDTO;
import com.ivanov_sergey.todoapp.exception_handling.UserAlreadyExistException;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.security.VerificationToken;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserDTO registerNewUserAccount(UserDTO userDTO);
    User registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void createVerificationToken(User user, String token);

    VerificationToken getVerificationToken(String token);
}
