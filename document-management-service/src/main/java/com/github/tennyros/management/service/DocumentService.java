package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DocumentService {

    Document createDocument(DocumentUploadRequest request, String objectName);

    void deleteDocument(String objectName);

    Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable);
}
