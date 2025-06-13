package com.github.tennyros.management.controller;

import com.github.tennyros.management.dto.version.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.service.DocumentOrchestrationService;
import com.github.tennyros.management.service.DocumentVersionService;
import com.github.tennyros.management.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/{documentId}/versions")
public class DocumentVersionController {

    private final MinioService minioService;
    private final DocumentVersionService documentVersionService;
    private final DocumentOrchestrationService documentOrchestrationService;

    @PostMapping
    public ResponseEntity<DocumentVersionResponse> uploadVersion(@PathVariable Long documentId,
                                                                 @Valid @ModelAttribute DocumentVersionUploadRequest request) {
        DocumentVersionResponse response = documentOrchestrationService.uploadDocumentVersion(documentId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{versionNumber}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long documentId, @PathVariable Long versionNumber) {
        DocumentVersion version = documentVersionService.getDocumentVersion(documentId, versionNumber);
        InputStream inputStream = minioService.download(version.getStorageKey());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + version.getFilename() + "\"")
                .contentType(MediaType.parseMediaType(version.getContentType()))
                .contentLength(version.getSize())
                .body(new InputStreamResource(inputStream));
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long documentId, @PathVariable Long versionId) {
        documentVersionService.deleteDocumentWithVersion(documentId, versionId);
        return ResponseEntity.noContent().build();
    }
}
