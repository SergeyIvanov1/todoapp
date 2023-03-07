package com.ivanov_sergey.todoapp.controller;

import com.ivanov_sergey.todoapp.dto.UserDTO;
import com.ivanov_sergey.todoapp.exception_handling.NoSuchTestEntityException;
import com.ivanov_sergey.todoapp.exception_handling.TaskIncorrectData;
import com.ivanov_sergey.todoapp.exception_handling.UserAlreadyExistException;
import com.ivanov_sergey.todoapp.persist.model.User;
import com.ivanov_sergey.todoapp.persist.repository.UserRepository;
import com.ivanov_sergey.todoapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class UserController {

    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(ApplicationEventPublisher eventPublisher, UserRepository userRepository, UserService userService) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Operation(summary = "Updating of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Not Found",
                    content = @Content)
    })
    @PutMapping("/users/{id}")
    public ResponseEntity<String> updateUser(@PathVariable("id") long id, @Valid @RequestBody UserDTO userDTO) {
        Optional<User> userData = userRepository.findById(id);

        if (userData.isPresent()) {
            User userFromDB = userData.get();
            userFromDB.setFirstName(userDTO.getFirstName());
            userFromDB.setLastName(userDTO.getLastName());
            userFromDB.setEmail(userDTO.getEmail());
            userFromDB.setPassword(userDTO.getPassword());
            userRepository.save(userFromDB);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Deleting an user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Deleting all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content)
    })
    @DeleteMapping("/users")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        try {
            userRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler
    public ResponseEntity<TaskIncorrectData> handleException(NoSuchTestEntityException exception) {
        TaskIncorrectData data = new TaskIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<TaskIncorrectData> handleException(Exception exception) {
        TaskIncorrectData data = new TaskIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserAlreadyExistException.class)
    public Map<String, String> handleUserAlreadyExistException(UserAlreadyExistException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "An account for that email already exists.");
        return errors;
    }
}
