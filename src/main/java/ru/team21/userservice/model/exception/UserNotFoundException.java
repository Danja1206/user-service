package ru.team21.userservice.model.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }
}
