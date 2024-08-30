package ru.team21.userservice.model.exception;

import org.springframework.http.HttpStatus;

public class PaymentProcessingException extends PaymentScheduleException {

    public PaymentProcessingException(String message, Throwable cause) {
        super(message, cause, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
