package com.ivanov_sergey.todoapp.exception_handling;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
