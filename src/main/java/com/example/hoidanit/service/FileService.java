package com.example.hoidanit.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

public interface FileService {
    void createDirectory(String folder) throws URISyntaxException;
    String storeFile(MultipartFile multipartFile, String folder) throws URISyntaxException, IOException;
}
