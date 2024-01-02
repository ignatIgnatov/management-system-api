package com.system.management.exception;

public class UserExistsException extends RuntimeException {
    public UserExistsException(String email) {
        super("User with email " + email + " already exists!");
    }
}
