package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.request.DocumentFilter;
import com.github.tennyros.management.dto.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;

public interface DocumentService {

    String uploadDocument(DocumentUploadRequest metadata);

    InputStream downloadDocument(String objectName);

    void deleteDocument(String objectName);

    Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable);

}
