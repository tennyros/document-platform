package com.github.tennyros.signing.service;

import com.github.tennyros.signing.dto.SignDocumentRequest;
import com.github.tennyros.signing.dto.SignDocumentResponse;

public interface SigningService {

    SignDocumentResponse signDocument(SignDocumentRequest request);

    boolean verifySignature(String documentId, String signatureId);
}
