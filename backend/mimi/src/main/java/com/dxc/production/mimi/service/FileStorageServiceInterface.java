package com.dxc.production.mimi.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.dxc.production.mimi.model.response.GenericResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageServiceInterface {
    public String save(MultipartFile file, String type);
    GenericResponse streamMedia(String url);

    public Resource load(String filename);
    public void deleteAll();
    public Stream<Path> loadAll();
}
