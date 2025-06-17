package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;
import com.github.tennyros.management.dto.version.response.DocumentVersionResponse;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;

public interface DocumentVersionService {

    void createInitialVersion(Document document, DocumentUploadRequest request, String objectName);

    DocumentVersion addNewVersion(Long documentId, DocumentVersionUploadRequest request, String objectName);

    DocumentVersionResponse getDocumentVersion(Long documentId, Long versionNumber);

    void deleteVersion(Long documentId, Long versionNumber);
}
