package com.ivanov_sergey.todoapp.repository;

import com.ivanov_sergey.todoapp.model.Tag;
import com.ivanov_sergey.todoapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
}
