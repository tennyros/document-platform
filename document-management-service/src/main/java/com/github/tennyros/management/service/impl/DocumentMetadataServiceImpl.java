package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.repository.jpa.DocumentRepository;
import com.github.tennyros.management.repository.jpa.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentMetadataService;
import com.github.tennyros.management.service.DocumentVersionMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentVersionMetadataService documentVersionMetadataService;

    @Override
    public void saveDocumentWithVersion(MultipartFile file, Document document, String objectName) {

        Optional<Document> existingDocumentOpt = documentRepository.findByTitle(document.getTitle());

        long newVersionNumber = 1L;
        Document savedDocument;

        if (existingDocumentOpt.isEmpty()) {
            document.setCreatedAt(LocalDateTime.now());
            savedDocument = documentRepository.save(document);
        } else {
            savedDocument = existingDocumentOpt.get();
            Optional<DocumentVersion> lastVersion = documentVersionRepository
                    .findTopByDocumentOrderByVersionNumberDesc(savedDocument);
            if (lastVersion.isPresent()) {
                newVersionNumber = lastVersion.get().getVersionNumber() + 1L;
            }
        }

        DocumentVersion documentVersion = DocumentVersion.builder()
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .storageKey(objectName)
                .versionNumber(newVersionNumber)
                .uploadedAt(LocalDateTime.now())
                .document(savedDocument)
                .build();

        DocumentVersion savedVersion = documentVersionRepository.save(documentVersion);

        Map<String, Object> defaultAttributes = Map.of(
                "status", "uploaded",
                "timestamp", LocalDateTime.now().toString()
        );

        documentVersionMetadataService.saveMetadata(savedVersion.getId(), defaultAttributes);
    }

    @Override
    public void deleteDocumentWithVersion(String objectName) {
        DocumentVersion version = documentVersionRepository.findByStorageKey(objectName)
                .orElseThrow(() -> new DocumentNotFoundException("File with object name " + objectName + " not found"));

        documentVersionMetadataService.deleteMetadata(version.getId());

        Document document = version.getDocument();
        documentRepository.delete(document);
    }
}
