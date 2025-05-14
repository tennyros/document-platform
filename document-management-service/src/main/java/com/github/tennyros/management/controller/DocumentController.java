package com.github.tennyros.management.controller;

import com.github.tennyros.management.dto.DocumentUploadRequestDto;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.service.impl.DocumentServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentServiceImpl documentServiceImpl;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@Valid @ModelAttribute DocumentUploadRequestDto metadata) {
        String objectName = documentServiceImpl.uploadDocument(metadata);
        return ResponseEntity.ok(objectName);
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<byte[]> download(@PathVariable String name) {
        log.debug("Fetching {} file", name);
        try {
            String contentType = Files.probeContentType(Paths.get(name));
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }
            try (InputStream inputStream = documentServiceImpl.downloadDocument(name)) {
                log.debug("Successfully fetched from Minio file: {}", name);
                byte[] bytes = inputStream.readAllBytes();
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                        .contentType(MediaType.parseMediaType(contentType))
                        .body(bytes);
            }
        } catch (IOException e) {
            log.warn("Error while reading {} file", name);
            throw new FileAccessDeniedException("Error while determining file type for " + name, e);
        }

    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> delete(@PathVariable String name) {
        log.info("Deleting {} document", name);
        documentServiceImpl.deleteDocument(name);
        return ResponseEntity.ok("Deleted: " + name);
    }
}
