package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.repository.jpa.DocumentRepository;
import com.github.tennyros.management.repository.jpa.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentVersionMetadataService;
import com.github.tennyros.management.service.DocumentVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentVersionServiceImpl implements DocumentVersionService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentVersionMetadataService documentVersionMetadataService;

    @Override
    public void createInitialVersion(Document document, DocumentUploadRequest request, String objectName) {

        DocumentVersion documentVersion =
                buildDocumentVersion(request.getFile(), objectName, document, 1L);

        DocumentVersion savedVersion = documentVersionRepository.save(documentVersion);

        Map<String, Object> defaultAttributes = Map.of(
                "status", "uploaded",
                "timestamp", LocalDateTime.now().toString()
        );

        documentVersionMetadataService.saveMetadata(savedVersion.getId(), defaultAttributes);
    }

    @Override
    public DocumentVersion addNewVersion(Long documentId, DocumentVersionUploadRequest request, String objectName) {
        Document savedDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Document with id %s not found", documentId)));

        Long latestVersionNumber = documentVersionRepository
                .findTopByDocumentOrderByVersionNumberDesc(savedDocument)
                .map(DocumentVersion::getVersionNumber)
                .orElse(0L);

        DocumentVersion newVersion =
                buildDocumentVersion(request.getFile(), objectName, savedDocument, latestVersionNumber + 1);

        return documentVersionRepository.save(newVersion);
    }

    @Override
    public DocumentVersion getDocumentVersion(Long documentId, Long versionNumber) {
        return documentVersionRepository.findByDocumentIdAndVersionNumber(documentId, versionNumber)
                .orElseThrow(() ->
                        new DocumentNotFoundException(String.format(
                                "For Document with id %d version id %d not found ", documentId, versionNumber)));
    }

    @Override
    public void deleteDocumentWithVersion(Long documentId, Long versionNumber) {
        Document savedDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Document with id %s not found", documentId)));

        documentVersionMetadataService.deleteMetadata(versionNumber);

        documentRepository.delete(savedDocument);
    }

    private DocumentVersion buildDocumentVersion(
            MultipartFile file, String objectName, Document document, Long version) {

        return DocumentVersion.builder()
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .storageKey(objectName)
                .versionNumber(version)
                .uploadedAt(LocalDateTime.now())
                .document(document)
                .build();
    }
}
