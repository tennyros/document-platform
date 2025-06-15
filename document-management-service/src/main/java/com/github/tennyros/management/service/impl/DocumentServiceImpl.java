package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.DocumentVersionInfo;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.repository.jpa.DocumentRepository;
import com.github.tennyros.management.repository.jpa.DocumentVersionRepository;
import com.github.tennyros.management.repository.mongo.DocumentVersionMetadataRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final MinioService minioService;
    private final DocumentVersionService documentVersionService;

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentVersionMetadataRepository documentVersionMetadataRepository;

    private final DocumentMapper documentMapper;

    @Override
    @Transactional
    public Document createDocument(DocumentUploadRequest request, String objectName) {
        Document entity = documentMapper.toEntity(request);
        entity.setCreatedAt(LocalDateTime.now());
        log.debug("Saving document entity with storageKey={} to database", objectName);
        Document savedDocument = documentRepository.save(entity);
        log.debug("Document saved successfully with id={}", savedDocument.getId());

        documentVersionService.createInitialVersion(savedDocument, request, objectName);

        return savedDocument;
    }

    @Override
    @Transactional
    public void deleteDocument(Long documentId) {
        if (!documentRepository.existsById(documentId)) {
            throw new DocumentNotFoundException(String.format("Document with id %s not found", documentId));
        }

        List<DocumentVersionInfo> versions = documentVersionRepository.findVersionInfoByDocumentId(documentId);

        versions.forEach(version -> minioService.delete(version.getStorageKey()));

        versions.forEach(version -> {
            log.debug("Deleting Mongo metadata for versionId={}", version.getId());
            documentVersionMetadataRepository.deleteByDocumentVersionId(version.getId());
        });

        documentVersionRepository.deleteAllByDocumentId(documentId);
        documentRepository.deleteByIdDirect(documentId);
        log.debug("Document with id={} and all versions successfully deleted", documentId);
    }

    @Override
    public Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable) {
        Specification<Document> spec = new DocumentSpecificationBuilder(filterDto).build();
        return documentRepository.findAll(spec, pageable);
    }
}
