package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.repository.jpa.DocumentRepository;
import com.github.tennyros.management.service.DocumentService;
import com.github.tennyros.management.service.DocumentVersionService;
import com.github.tennyros.management.service.MinioService;
import com.github.tennyros.management.specification.DocumentSpecificationBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final MinioService minioService;
    private final DocumentVersionService documentVersionService;

    private final DocumentRepository documentRepository;

    private final DocumentMapper documentMapper;

    @Override
    public Document createDocument(DocumentUploadRequest request, String objectName) {
        Document entity = documentMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        Document savedDocument = documentRepository.save(entity);

        documentVersionService.createInitialVersion(savedDocument, request, objectName);

        return savedDocument;
    }

    @Override
    public void deleteDocument(String objectName) {
        minioService.delete(objectName);
    }

    @Override
    public Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable) {
        Specification<Document> spec = new DocumentSpecificationBuilder(filterDto).build();
        return documentRepository.findAll(spec, pageable);
    }
}
