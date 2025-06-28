package com.github.tennyros.management.service;

import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.entity.DocumentVersionMetadata;

import java.util.Map;
import java.util.Optional;

/**
 * Service for managing {@link DocumentVersionMetadata} entity of certain Document Version.
 * */
public interface DocumentVersionMetadataService {

    /**
     * Creates and saves {@link DocumentVersionMetadata} of certain Document Version ID.
     *
     * @param documentVersionId the ID of Document Version entity
     * @param attributes extra attributes of certain Document Version
     * */
    void saveMetadata(Long documentVersionId, Map<String, Object> attributes);

    /**
     * Retrieves Document Version Metadata.
     *
     * @param documentVersionId the ID of Document Version entity
     *
     * @return the Optional of Document Version Metadata entity
     * */
    Optional<DocumentVersionMetadata> getMetadata(Long documentVersionId);

    /**
     * Deletes {@link DocumentVersionMetadata}, without extra logic (wrapper for repository method).
     *
     * @param documentVersionId the ID of the {@link DocumentVersion} entity
     * */
    void deleteByDocumentVersionId(Long documentVersionId);
}