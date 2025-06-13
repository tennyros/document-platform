package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;

public interface DocumentVersionService {

    void createInitialVersion(Document document, DocumentUploadRequest request, String objectName);

    DocumentVersion addNewVersion(Long documentId, DocumentVersionUploadRequest request, String objectName);

    DocumentVersion getDocumentVersion(Long documentId, Long versionNumber);

    void deleteDocumentWithVersion(Long documentId, Long versionNumber);
}
