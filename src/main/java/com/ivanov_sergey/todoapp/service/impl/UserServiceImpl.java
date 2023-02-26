package com.ivanov_sergey.todoapp.service.impl;

import com.ivanov_sergey.todoapp.dto.UserDTO;
import com.ivanov_sergey.todoapp.exception_handling.UserAlreadyExistException;
import com.ivanov_sergey.todoapp.model.Role;
import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.repository.UserRepository;
import com.ivanov_sergey.todoapp.repository.VerificationTokenRepository;
import com.ivanov_sergey.todoapp.model.VerificationToken;
import com.ivanov_sergey.todoapp.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, VerificationTokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public User registerNewUserAccount(UserDTO userDTO) throws UserAlreadyExistException{
        return userRepository.save(User.builder()
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .role(new Role("USER"))
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
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void addToDBVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }
}
