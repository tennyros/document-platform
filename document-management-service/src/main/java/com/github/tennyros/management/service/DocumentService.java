package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.DocumentUploadRequestDto;

import java.io.InputStream;

public interface DocumentService {

    String uploadDocument(DocumentUploadRequestDto metadata);

    InputStream downloadDocument(String objectName);

    void deleteDocument(String objectName);

}
