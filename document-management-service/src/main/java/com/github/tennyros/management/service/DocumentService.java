package com.github.tennyros.management.service;

import com.github.tennyros.management.model.Document;
import com.github.tennyros.management.model.DocumentVersion;
import com.github.tennyros.management.repository.DocumentRepository;
import com.github.tennyros.management.repository.DocumentVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

    private final MinioService minioService;

    public String uploadDocument(MultipartFile file, String title, String description) throws Exception {
        String objectName = minioService.upload(file);

        Document document = Document.builder()
                .title(title)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();

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

        return objectName;
    }

    public InputStream downloadDocument(String objectName) throws Exception {
        return minioService.download(objectName);
    }

    public void deleteDocument(String objectName) throws Exception {
        minioService.delete(objectName);
    }

}
