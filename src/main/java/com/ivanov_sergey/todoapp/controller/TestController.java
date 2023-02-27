package com.ivanov_sergey.todoapp.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*Существует 4 API:
        — /api/test/allдля общего доступа
        — /api/test/userдля пользователей ROLE_USERили ROLE_MODERATOR— для пользователей есть ROLE_ADMIN
        — для пользователей есть/api/test/modROLE_MODERATOR
        /api/test/adminROLE_ADMIN

        Вы помните, что мы использовали @EnableGlobalMethodSecurity(prePostEnabled = true)для WebSecurityConfigкласса?

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { ... }
    Теперь мы можем легко защищать методы в нашем API с помощью @PreAuthorizeаннотаций.*/

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
