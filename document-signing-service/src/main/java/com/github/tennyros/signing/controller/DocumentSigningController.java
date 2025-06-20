package com.github.tennyros.signing.controller;

import com.github.tennyros.signing.dto.SignDocumentRequest;
import com.github.tennyros.signing.dto.SignDocumentResponse;
import com.github.tennyros.signing.service.SigningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/signatures")
public class DocumentSigningController {

    private final SigningService signingService;

    @PostMapping("/sign")
    public ResponseEntity<SignDocumentResponse> signDocument(@Valid @RequestBody SignDocumentRequest request) {
        log.info("Signing Document with documentId={} and versionId={}",
                request.getDocumentId(), request.getVersionId());

        SignDocumentResponse response = signingService.signDocument(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<Boolean> verifySignature(
            @RequestParam String documentId,
            @RequestParam String signatureId) {
        log.info("Verifying signature for document: {}", documentId);
        boolean isValid = signingService.verifySignature(documentId, signatureId);
        return ResponseEntity.ok(isValid);
    }
}