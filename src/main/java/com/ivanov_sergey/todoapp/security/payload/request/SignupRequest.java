package com.ivanov_sergey.todoapp.security.payload.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Set;

@Getter
@Setter
public class SignupRequest {
//    @NotBlank
//    @Size(min = 3, max = 20)
//    private String username;
//
//    @NotBlank
//    @Size(max = 50)
//    @Email
//    private String email;

    private Set<String> role;

//    @NotBlank
//    @Size(min = 6, max = 40)
//    private String password;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email: require symbols A-Z, a-z, 0-9, symbols: +_.-@.")
    private String email;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё]{2,25}", message = "Username can contains only letters and be between 2 and 25 characters long")
    private String username;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё]{2,25}", message = "First name can contains only letters and be between 2 and 25 characters long")
    private String firstName;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "[A-Za-zА-Яа-яЁё]{2,25}", message = "Last name can contains only letters and be between 2 and 45 characters long")
    private String lastName;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*]{6,}$",
            message = "Invalid password. String must contains min: 1 number, 1 symbol (!@#$%^&*), " +
                    "1 latter upper case, 1 latter lower case, all count - min 6 symbols")
    private String password;
}
