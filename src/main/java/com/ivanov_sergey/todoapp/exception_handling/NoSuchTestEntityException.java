package com.ivanov_sergey.todoapp.exception_handling;

public class NoSuchTestEntityException extends RuntimeException{

    public NoSuchTestEntityException(String message) {
        super(message);
    }
}
