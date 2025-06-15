package com.github.tennyros.management.controller;

import com.github.tennyros.management.dto.version.response.DocumentFileDto;
import com.github.tennyros.management.dto.version.response.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;
import com.github.tennyros.management.service.DocumentOrchestrationService;
import com.github.tennyros.management.service.DocumentVersionService;
import com.github.tennyros.management.util.ContentDispositionUtil;
import com.github.tennyros.management.util.ResponseFactory;
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
import java.io.BufferedInputStream;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/{documentId}/versions")
public class DocumentVersionController {

    private final DocumentVersionService documentVersionService;
    private final DocumentOrchestrationService documentOrchestrationService;

    @PostMapping
    public ResponseEntity<DocumentVersionResponse> uploadVersion(@PathVariable Long documentId,
                                                                 @Valid @ModelAttribute DocumentVersionUploadRequest request) {
        log.info("Uploading New Version for Document with id={}", documentId);
        DocumentVersionResponse response = documentOrchestrationService.uploadDocumentVersion(documentId, request);
        log.info("Successfully uploaded New Version with id={} for Document with id={}", response.getId(), documentId);
        return ResponseFactory.created(response.getId(), response);
    }

    @GetMapping("/{versionNumber}/download")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long documentId, @PathVariable Long versionNumber) {
        log.info("Downloading Version with id={} for Document with id={}", versionNumber, documentId);
        DocumentFileDto fileDto = documentOrchestrationService.downloadDocumentVersion(documentId, versionNumber);
        log.info("Successfully Downloaded Version with id={} for Document with id={}", versionNumber, documentId);
        String contentDisposition = ContentDispositionUtil.buildContentDisposition(fileDto.getFilename());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType(fileDto.getContentType()))
                .contentLength(fileDto.getSize())
                .body(new InputStreamResource(new BufferedInputStream(fileDto.getInputStream())));
    }

    @DeleteMapping("/{versionId}")
    public ResponseEntity<Void> deleteVersion(@PathVariable Long documentId, @PathVariable Long versionId) {
        log.info("Deleting Version with id={} of Document with id={}", versionId, documentId);
        documentVersionService.deleteVersion(documentId, versionId);
        log.info("Successfully Deleted Version with id={} of Document with id={}", versionId, documentId);
        return ResponseFactory.noContent();
    }
}
