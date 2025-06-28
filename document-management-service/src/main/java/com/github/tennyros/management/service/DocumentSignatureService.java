package com.github.tennyros.management.service;

import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentSignature;
import com.github.tennyros.management.entity.DocumentVersion;

/**
 * Service for managing Documents Signatures for certain Document Versions.
 * */
public interface DocumentSignatureService {

    /**
     * Creates and saves {@link DocumentSignature} according to signature and certificate data,
     * that retrieving from Document Signing Service.
     *
     * @param documentId the ID of the {@link Document}
     * @param versionId the ID of the {@link DocumentVersion} of certain {@link Document}
     * @param request the DTO containing signature and certificate data
     *
     * @return the ID of saved {@link DocumentSignature}
     * */
    Long saveSignature(Long documentId, Long versionId, SaveSignatureRequest request);
}
