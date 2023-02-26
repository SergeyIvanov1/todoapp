package com.ivanov_sergey.todoapp.service;

import com.ivanov_sergey.todoapp.dto.UserDTO;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.model.VerificationToken;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User registerNewUserAccount(UserDTO userDTO);
//    User registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException;

    com.ivanov_sergey.todoapp.model.User getUser(String verificationToken);

    void saveRegisteredUser(com.ivanov_sergey.todoapp.model.User user);

    void addToDBVerificationToken(com.ivanov_sergey.todoapp.model.User user, String token);

    VerificationToken getVerificationToken(String token);
}
