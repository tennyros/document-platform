package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.request.DocumentFilter;
import com.github.tennyros.management.dto.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentUploadProcessException;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.repository.DocumentRepository;
import com.github.tennyros.management.repository.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentService;
import com.github.tennyros.management.specification.DocumentSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final MinioServiceImpl minioServiceImpl;
    private final DocumentMetadataServiceImpl metadataService;

    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    @Override
    public String uploadDocument(DocumentUploadRequest metadata) {
        MultipartFile multipartFile = metadata.getFile();
        String objectName = minioServiceImpl.upload(multipartFile);

        try {
            Document document = documentMapper.toEntity(metadata);
            metadataService.saveDocumentWithVersion(multipartFile, document, objectName);
            return objectName;
        } catch (Exception e) {
            log.warn("Error occurred while saving metadata, rolling back file from storage", e);
            minioServiceImpl.delete(objectName);
            throw new DocumentUploadProcessException("Error during document upload and metadata saving", e);
        }
    }

    @Override
    public InputStream downloadDocument(String objectName) {
        if (!documentVersionRepository.existsByStorageKey(objectName)) {
            throw new DocumentNotFoundException("File with object name " + objectName + " not found");
        }
        return minioServiceImpl.download(objectName);
    }

    @Override
    public void deleteDocument(String objectName) {
        minioServiceImpl.delete(objectName);
        metadataService.deleteDocumentWithVersion(objectName);
    }

    @Override
    public Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable) {
        Specification<Document> spec = DocumentSpecificationBuilder.build(filterDto);
        return documentRepository.findAll(spec, pageable);
    }

}
