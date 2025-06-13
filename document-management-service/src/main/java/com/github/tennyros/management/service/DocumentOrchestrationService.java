package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.document.response.DocumentResponse;
import com.github.tennyros.management.dto.version.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.DocumentVersionUploadRequest;

public interface DocumentOrchestrationService {

    DocumentResponse uploadDocument(DocumentUploadRequest request);

    DocumentVersionResponse uploadDocumentVersion(Long documentId, DocumentVersionUploadRequest request);
}
