package com.github.tennyros.management.service;

import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.model.Document;
import com.github.tennyros.management.model.DocumentVersion;
import com.github.tennyros.management.repository.DocumentRepository;
import com.github.tennyros.management.repository.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentMetadataService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

    public void saveDocumentWithVersion(MultipartFile file, Document document, String objectName) {

        document.setCreatedAt(LocalDateTime.now());
        documentRepository.save(document);

        DocumentVersion documentVersion = DocumentVersion.builder()
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .storageKey(objectName)
                .versionLabel("v1")
                .uploadedAt(LocalDateTime.now())
                .document(document)
                .build();

        documentVersionRepository.save(documentVersion);

    }

    public void deleteDocumentWithVersion(String objectName) {
        DocumentVersion version = documentVersionRepository.findByStorageKey(objectName)
                .orElseThrow(() -> new DocumentNotFoundException("File with object name " + objectName + " not found"));

        Document document = version.getDocument();
        documentRepository.delete(document);
    }

}
