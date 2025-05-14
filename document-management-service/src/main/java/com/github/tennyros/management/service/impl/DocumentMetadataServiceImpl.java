package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.repository.DocumentRepository;
import com.github.tennyros.management.repository.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentMetadataServiceImpl implements DocumentMetadataService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

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

        documentVersionRepository.save(documentVersion);
    }

    @Override
    public void deleteDocumentWithVersion(String objectName) {
        DocumentVersion version = documentVersionRepository.findByStorageKey(objectName)
                .orElseThrow(() -> new DocumentNotFoundException("File with object name " + objectName + " not found"));

        Document document = version.getDocument();
        documentRepository.delete(document);
    }

}
