package ru.team21.userservice.model.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PaymentScheduleException extends RuntimeException {

    private final HttpStatus status;

    public PaymentScheduleException(String message, HttpStatus status) {
        this(message, null, status);
    }

    public PaymentScheduleException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }
}
