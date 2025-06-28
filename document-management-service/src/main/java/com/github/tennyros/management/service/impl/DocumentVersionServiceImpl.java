package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.DocumentVersionInfo;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentVersionNotFoundException;
import com.github.tennyros.management.mapper.DocumentVersionMapper;
import com.github.tennyros.management.repository.jpa.DocumentRepository;
import com.github.tennyros.management.repository.jpa.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentVersionMetadataService;
import com.github.tennyros.management.service.DocumentVersionService;
import com.github.tennyros.management.service.HashService;
import com.github.tennyros.management.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentVersionServiceImpl implements DocumentVersionService {

    private final MinioService minioService;
    private final DocumentVersionMetadataService documentVersionMetadataService;
    private final HashService hashService;

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;

    private final DocumentVersionMapper documentVersionMapper;

    @Override
    @Transactional
    public void createInitialVersion(Document document, DocumentUploadRequest request, String objectName) {
        log.debug("Creating and saving initial version for document with id={} to PostgreSQL", document.getId());
        DocumentVersion documentVersion =
                buildDocumentVersion(request.getFile(), objectName, document, 1L);

        DocumentVersion savedVersion = documentVersionRepository.save(documentVersion);

        log.debug("Initial version with id={} saved successfully to PostgreSQL", savedVersion.getId());

        Map<String, Object> defaultAttributes = Map.of(
                "status", "uploaded",
                "timestamp", LocalDateTime.now().toString()
        );

        documentVersionMetadataService.saveMetadata(savedVersion.getId(), defaultAttributes);
    }

    @Override
    @Transactional
    public DocumentVersion addNewVersion(Long documentId, DocumentVersionUploadRequest request, String objectName) {
        log.debug("Creating and saving New Version with storageKey={} for document with id={} to PostgreSQL",
                objectName, documentId);

        Document savedDocument = documentRepository.findById(documentId)
                .orElseThrow(() -> new DocumentNotFoundException(String.format("Document with id %s not found", documentId)));

        Long latestVersionNumber = documentVersionRepository
                .findTopByDocumentOrderByVersionNumberDesc(savedDocument)
                .map(DocumentVersion::getVersionNumber)
                .orElse(0L);

        DocumentVersion newVersion =
                buildDocumentVersion(request.getFile(), objectName, savedDocument, latestVersionNumber + 1);

        DocumentVersion savedVersion = documentVersionRepository.save(newVersion);

        Map<String, Object> defaultAttributes = Map.of(
                "status", "uploaded",
                "timestamp", LocalDateTime.now().toString()
        );

        documentVersionMetadataService.saveMetadata(savedVersion.getId(), defaultAttributes);

        return savedVersion;
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentVersion getDocumentVersion(Long documentId, Long versionNumber) {
        log.debug("Fetching information about Document Version with id={} of Document with id={} from PostgreSQL",
                versionNumber, documentId);
        return documentVersionRepository.findByDocumentIdAndVersionNumber(documentId, versionNumber)
                .orElseThrow(() ->
                        new DocumentNotFoundException(String.format(
                                "Version id %d of Document with id %d not found ", versionNumber, documentId)));
    }

    @Override
    @Transactional
    public void deleteVersion(Long documentId, Long versionNumber) {
        if (!documentRepository.existsById(documentId)) {
            throw new DocumentNotFoundException(String.format("Document with id %s not found", documentId));
        }

        Optional<DocumentVersionInfo> versionInfo = documentVersionRepository
                .findVersionsInfoByDocumentId(documentId).stream()
                .filter(v -> v.getVersionNumber().equals(versionNumber))
                .findFirst();

        if (versionInfo.isEmpty()) {
            throw new DocumentVersionNotFoundException(String.format(
                    "Version %s of Document %s not found", versionNumber, documentId));
        }

        minioService.delete(versionInfo.get().getStorageKey());

        log.debug("Deleting Metadata for Document Version with id={} from MongoDB", versionNumber);
        documentVersionMetadataService.deleteByDocumentVersionId(versionNumber);
        log.debug("Successfully deleted Metadata for Document Version with id={} from MongoDB", versionNumber);

        int deleted = documentVersionRepository.deleteByDocumentIdAndVersionNumberDirect(documentId, versionNumber);
        if (deleted == 0) {
            throw new DocumentVersionNotFoundException(String.format("Failed to delete version %s", versionNumber));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentVersionInfo> getVersionsInfoByDocumentId(Long documentId) {
        return documentVersionRepository.findVersionsInfoByDocumentId(documentId);
    }

    @Override
    @Transactional
    public void deleteAllByDocumentId(Long documentId) {
        documentVersionRepository.deleteAllByDocumentId(documentId);
    }

    private DocumentVersion buildDocumentVersion(
            MultipartFile file, String objectName, Document document, Long version) {

        String hash;
        try (InputStream inputStream = file.getInputStream()) {
            hash = hashService.calculateSHA256(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read file for hash calculation", e);
        }

        return DocumentVersion.builder()
                .filename(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize())
                .storageKey(objectName)
                .versionNumber(version)
                .uploadedAt(LocalDateTime.now())
                .document(document)
                .hash(hash)
                .build();
    }
}
