package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.exception.fileexception.FileDownloadException;
import com.example.springbootecommerce.exception.fileexception.FileUploadException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileAwsServiceInterface {
    String uploadFile(MultipartFile multipartFile) throws FileUploadException, IOException;

    Object downloadFile(String fileName) throws FileDownloadException, IOException;

    boolean delete(String fileName);
}
