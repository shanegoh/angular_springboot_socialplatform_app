package com.dxc.production.mimi.service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class FileStorageService implements FileStorageServiceInterface{
    private final Path root = Paths.get("uploads");

    @Override
    public String save(MultipartFile file, String suffix) {
        try {
            String storageName = "uploads"; // Set a temp file
            File tempFile = File.createTempFile("Media", suffix, new File(storageName));   // Set file path
            Path filepath = Paths.get(storageName, tempFile.getName());
            try (OutputStream os = Files.newOutputStream(filepath)) {   // Write uploaded MultipartFile to the temp File.
                os.write(file.getBytes());  // write
            } catch (Exception e) {
                throw new RuntimeException("Could not write file. Error: " + e.getMessage());
            }
            // Convert temp file to stream
            InputStream stream = new FileInputStream(tempFile);
            // Construct a new MultipartFile with stream data and tempFile name
            MultipartFile multipartFile = new MockMultipartFile("file",
                    tempFile.getName(), file.getContentType(), stream);
            tempFile.delete();  // Delete the temp file, no longer needed.

            // Store the MultipartFile
            Files.copy(multipartFile.getInputStream(), this.root.resolve(filepath.getFileName()));

            return filepath.getParent() + "/" + filepath.getFileName(); // return path
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
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
