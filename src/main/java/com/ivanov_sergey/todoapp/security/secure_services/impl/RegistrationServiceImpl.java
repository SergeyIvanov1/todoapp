package com.ivanov_sergey.todoapp.security.secure_services.impl;

import com.ivanov_sergey.todoapp.enums.ERole;
import com.ivanov_sergey.todoapp.persist.model.Role;
import com.ivanov_sergey.todoapp.persist.model.User;
import com.ivanov_sergey.todoapp.persist.model.VerificationToken;
import com.ivanov_sergey.todoapp.persist.repository.RoleRepository;
import com.ivanov_sergey.todoapp.persist.repository.VerificationTokenRepository;
import com.ivanov_sergey.todoapp.security.payload.request.SignupRequest;
import com.ivanov_sergey.todoapp.security.secure_services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;

    @Autowired
    public RegistrationServiceImpl(RoleRepository roleRepository, VerificationTokenRepository tokenRepository) {
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void addToDBVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    @Override
    public Set<Role> getRolesFromRequest(SignupRequest signUpRequest){
        return getRoles(signUpRequest);
    }

    @Override
    public User getUser(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }

    private Set<Role> getRoles(SignupRequest signUpRequest) {
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        return roles;
    }
}
