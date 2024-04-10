package com.example.springbootecommerce.exception.fileexception;

public class FileDownloadException extends SpringBootFileUploadException {
    public FileDownloadException(String message) {
        super(message);
    }
}
