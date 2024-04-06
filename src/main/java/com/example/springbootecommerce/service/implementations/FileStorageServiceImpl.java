package com.example.springbootecommerce.service.implementations;

import com.example.springbootecommerce.model.FileInfo;
import com.example.springbootecommerce.service.interfaces.FileStorageServiceInterface;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageServiceInterface {
    private final Path root = Paths.get("src/main/resources/static/uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    public FileInfo save(MultipartFile file) {
        try {
            // Ensure directory exists, create if it doesn't
            init();
            // Generate a unique filename to prevent collisions
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = this.root.resolve(fileName);
            // Check if the file already exists
            if (Files.exists(filePath)) {
                throw new FileAlreadyExistsException("A file with the same name already exists.");
            }
            // Save the file
            Files.copy(file.getInputStream(), filePath);
            // Construct file info
            String fileUrl = "http://localhost:8080/uploads/" + fileName;
            return new FileInfo(fileName, fileUrl);
        } catch (FileAlreadyExistsException e) {
            throw new RuntimeException("A file of that name already exists.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }

    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public boolean delete(String filename) {
        try {
            Path file = root.resolve(filename);
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
