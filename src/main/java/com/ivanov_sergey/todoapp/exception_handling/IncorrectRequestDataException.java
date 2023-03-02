package com.ivanov_sergey.todoapp.exception_handling;

public class IncorrectRequestDataException extends RuntimeException{

    public IncorrectRequestDataException(String message) {
        super(message);
    }
}
