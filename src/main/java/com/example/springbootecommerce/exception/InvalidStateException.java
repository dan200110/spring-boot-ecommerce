package com.example.springbootecommerce.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidStateException extends RuntimeException {

    public InvalidStateException(String s) {
        super(s);
    }
}