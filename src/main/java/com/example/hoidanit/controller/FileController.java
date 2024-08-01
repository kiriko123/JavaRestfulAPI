package com.example.hoidanit.controller;

import com.example.hoidanit.dto.response.FileResponse;
import com.example.hoidanit.service.FileService;
import com.example.hoidanit.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

    @Value("${khang.upload-file.base-uri}")
    private String baseUri;

    private final FileService fileService;

    @PostMapping("/files")
    @ApiMessage("Upload single file")

    public ResponseEntity<?> uploadFile(@RequestParam(name = "file", required = true) MultipartFile file,
                                     @RequestParam("folder") String folder) throws URISyntaxException, IOException {

        //validate

        if (file.isEmpty()) {
            throw new FileUploadException("File is empty");
        }

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");

        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new FileUploadException("File is not valid");
        }

        //create a directory if not exist
        fileService.createDirectory(baseUri + folder);
        //store file
        String uploadFile =  fileService.storeFile(file, folder);



        return ResponseEntity.status(HttpStatus.CREATED).body(FileResponse.builder()
                        .fileName(uploadFile)
                        .uploadedAt(Instant.now())
                        .build());
    }
}
