package com.github.tennyros.management.controller;

import com.github.tennyros.dto.DocumentHashResponse;
import com.github.tennyros.management.dto.version.response.DocumentVersionResponse;
import com.github.tennyros.management.service.DocumentVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/{documentId}/versions/{versionId}/hash")
public class DocumentVersionHashController {

    private final DocumentVersionService documentVersionService;

    @GetMapping
    public ResponseEntity<DocumentHashResponse> getHash(@PathVariable Long documentId, @PathVariable Long versionId) {
        DocumentVersionResponse documentVersion = documentVersionService.getDocumentVersion(documentId, versionId);
        DocumentHashResponse response = DocumentHashResponse.builder()
                .documentId(documentId)
                .versionId(versionId)
                .hash(documentVersion.getHash())
                .build();

        return ResponseEntity.ok(response);
    }
}
