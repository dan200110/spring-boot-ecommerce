package com.example.springbootecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ResourceDuplicateException extends RuntimeException{
    public ResourceDuplicateException(String s) {
        super(s);
    }
}
