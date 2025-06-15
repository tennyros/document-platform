package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.document.response.DocumentResponse;
import com.github.tennyros.management.dto.version.response.DocumentFileDto;
import com.github.tennyros.management.dto.version.response.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;

public interface DocumentOrchestrationService {

    DocumentResponse uploadDocument(DocumentUploadRequest request);

    DocumentVersionResponse uploadDocumentVersion(Long documentId, DocumentVersionUploadRequest request);

    DocumentFileDto downloadDocumentVersion(Long documentId, Long versionNumber);
}
