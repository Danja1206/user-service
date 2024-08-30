package ru.team21.userservice.model.exception;

import org.springframework.http.HttpStatus;

public class PaymentNotFoundException extends PaymentScheduleException {

    public PaymentNotFoundException(Long id) {
        super("Payment with id " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
