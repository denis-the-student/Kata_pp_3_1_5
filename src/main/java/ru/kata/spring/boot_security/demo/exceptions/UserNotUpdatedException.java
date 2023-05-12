package ru.kata.spring.boot_security.demo.exceptions;

public class UserNotUpdatedException extends RuntimeException{
    public UserNotUpdatedException(String message) {
        super(message);
    }
}
