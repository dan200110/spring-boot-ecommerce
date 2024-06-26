package com.example.springbootecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordChangeException extends RuntimeException{
    public PasswordChangeException(String message) {
        super(message);
    }
}
