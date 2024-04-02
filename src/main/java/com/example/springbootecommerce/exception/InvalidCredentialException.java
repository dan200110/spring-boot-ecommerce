package com.example.springbootecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidCredentialException extends RuntimeException {

    public InvalidCredentialException(String s) {
        super(s);
    }
}