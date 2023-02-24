package com.ivanov_sergey.todoapp.repository;

import com.ivanov_sergey.todoapp.model.User;
import com.ivanov_sergey.todoapp.security.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
