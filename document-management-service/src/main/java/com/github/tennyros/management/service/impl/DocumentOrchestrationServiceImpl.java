package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.document.response.DocumentResponse;
import com.github.tennyros.management.dto.version.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.DocumentUploadProcessException;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.mapper.DocumentVersionMapper;
import com.github.tennyros.management.service.DocumentOrchestrationService;
import com.github.tennyros.management.service.DocumentService;
import com.github.tennyros.management.service.DocumentVersionService;
import com.github.tennyros.management.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentOrchestrationServiceImpl implements DocumentOrchestrationService {
    private final DocumentMapper documentMapper;

    private final MinioService minioService;
    private final DocumentService documentService;
    private final DocumentVersionService documentVersionService;

    private final DocumentVersionMapper documentVersionMapper;

    @Override
    public DocumentResponse uploadDocument(DocumentUploadRequest request) {
        MultipartFile multipartFile = request.getFile();
        String objectName = minioService.upload(multipartFile);

        try {
            Document document = documentService.createDocument(request, objectName);
            return documentMapper.toDto(document);
        } catch (Exception e) {
            log.warn("Error occurred while saving metadata, rolling back file from storage", e);
            minioService.delete(objectName);
            throw new DocumentUploadProcessException("Error during document upload and metadata saving", e);
        }
    }

    @Override
    public DocumentVersionResponse uploadDocumentVersion(Long documentId, DocumentVersionUploadRequest request) {
        MultipartFile multipartFile = request.getFile();
        String objectName = minioService.upload(multipartFile);

        try {
            DocumentVersion documentVersion = documentVersionService.addNewVersion(documentId, request, objectName);
            return documentVersionMapper.toDto(documentVersion);
        } catch (Exception e) {
            log.warn("Error occurred while saving metadata, rolling back file from storage", e);
            minioService.delete(objectName);
            throw new DocumentUploadProcessException("Error during document upload and metadata saving", e);
        }
    }

}
