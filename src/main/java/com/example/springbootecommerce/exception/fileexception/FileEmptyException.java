package com.example.springbootecommerce.exception.fileexception;

public class FileEmptyException extends SpringBootFileUploadException {
    public FileEmptyException(String message) {
        super(message);
    }
}

