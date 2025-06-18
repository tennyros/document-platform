package com.github.tennyros.management.controller;

import com.github.tennyros.dto.DocumentSignatureResponse;
import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.management.service.DocumentSignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents/{documentId}/versions/{versionId}/signatures")
public class DocumentSignatureController {

    private final DocumentSignatureService documentSignatureService;

    @PostMapping
    public ResponseEntity<DocumentSignatureResponse> saveSignature(@PathVariable Long documentId,
                                                                   @PathVariable Long versionId,
                                                                   @RequestBody @Valid SaveSignatureRequest request) {
        Long savedSignatureId = documentSignatureService.saveSignature(documentId, versionId, request);
        return ResponseEntity.ok(new DocumentSignatureResponse(savedSignatureId));
    }
}
