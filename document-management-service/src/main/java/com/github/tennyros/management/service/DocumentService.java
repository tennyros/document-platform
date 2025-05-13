package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.DocumentUploadRequestDto;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentUploadProcessException;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.model.Document;
import com.github.tennyros.management.repository.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentService {

    private final MinioService minioService;
    private final DocumentMetadataService metadataService;

    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentMapper documentMapper;

    public String uploadDocument(DocumentUploadRequestDto metadata) {
        MultipartFile multipartFile = metadata.getFile();
        String objectName = minioService.upload(multipartFile);

        try {
            Document document = documentMapper.toEntity(metadata);
            metadataService.saveDocumentWithVersion(multipartFile, document, objectName);
            return objectName;
        } catch (Exception e) {
            log.warn("Error occurred while saving metadata, rolling back file from storage", e);
            minioService.delete(objectName);
            throw new DocumentUploadProcessException("Error during document upload and metadata saving", e);
        }
    }

    public InputStream downloadDocument(String objectName) {
        if (!documentVersionRepository.existsByStorageKey(objectName)) {
            throw new DocumentNotFoundException("File with object name " + objectName + " not found");
        }
        return minioService.download(objectName);
    }

    public void deleteDocument(String objectName) {
        minioService.delete(objectName);
        metadataService.deleteDocumentWithVersion(objectName);
    }

}
