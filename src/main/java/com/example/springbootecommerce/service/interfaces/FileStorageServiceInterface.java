package com.example.springbootecommerce.service.interfaces;

import com.example.springbootecommerce.model.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface FileStorageServiceInterface {
    public void init();

    public FileInfo save(MultipartFile file);

    public Resource load(String filename);

    public boolean delete(String filename);

    public void deleteAll();

    public Stream<Path> loadAll();
}
