package ru.team21.userservice.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class AuthenticationException extends RuntimeException {

    private final HttpStatus status;

    public AuthenticationException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

}
