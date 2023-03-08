package com.ivanov_sergey.todoapp.exception_handling;

public class NoSuchEntityException extends RuntimeException{

    public NoSuchEntityException(String message) {
        super(message);
    }
}
