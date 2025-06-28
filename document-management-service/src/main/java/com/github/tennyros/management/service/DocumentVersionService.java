package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.version.DocumentVersionInfo;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentVersionNotFoundException;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Service for managing Document Version of certain Document.
 */
public interface DocumentVersionService {

    /**
     * Creates and saves an initial version of a document, including its metadata and SHA-256 hash.
     *
     * @param document the parent {@link Document} entity to associate the version with
     * @param request the upload request containing file and metadata
     * @param objectName the generated storage key for the uploaded file in MinIO
     *
     * @throws IllegalStateException if the uploaded file cannot be read for hash calculation
     */
    void createInitialVersion(Document document, DocumentUploadRequest request, String objectName);

    /**
     * Creates and saves a new version of the specified document, including its metadata and SHA-256 hash.
     * <p>
     * The new version is assigned a version number based on the latest existing version of the document.
     * Also saves default metadata attributes (e.g., status and timestamp) associated with the version.
     *
     * @param documentId the ID of the existing {@link Document} to which the new version will be linked
     * @param request the upload request containing the new file version
     * @param objectName the generated storage key for the uploaded file in MinIO
     * @return the saved {@link DocumentVersion} entity
     *
     * @throws DocumentNotFoundException if the document with the specified ID does not exist
     * @throws IllegalStateException if the uploaded file cannot be read for hash calculation
     */
    DocumentVersion addNewVersion(Long documentId, DocumentVersionUploadRequest request, String objectName);

    /**
     * Retrieves detailed information about a specific version of a document.
     *
     * @param documentId the ID of the {@link Document} associated with the version
     * @param versionNumber the version number to retrieve
     * @return the {@link DocumentVersion} entity containing version metadata and file details
     *
     * @throws DocumentNotFoundException if the specified document or version does not exist
     */
    DocumentVersion getDocumentVersion(Long documentId, Long versionNumber);

    /**
     * Deletes a specific version of a document along with its associated metadata and file in MinIO.
     * <p>
     * The method performs the following steps:
     * <ul>
     *   <li>Validates that the parent document exists</li>
     *   <li>Checks that the version exists for the given document</li>
     *   <li>Deletes the file from MinIO storage</li>
     *   <li>Deletes version metadata</li>
     *   <li>Deletes the version record from the database</li>
     * </ul>
     *
     * @param documentId the ID of the parent {@link Document}
     * @param versionNumber the version number to delete
     *
     * @throws DocumentNotFoundException if the parent document does not exist
     * @throws DocumentVersionNotFoundException if the specified version is not found
     */
    void deleteVersion(Long documentId, Long versionNumber);

    /**
     * Retrieves List of {@link DocumentVersionInfo} DTOs, without extra logic (wrapper for repository method).
     *
     * @param documentId the ID of the {@link DocumentVersion}
     * @return the List of {@link DocumentVersionInfo} DTOs
     * */
    List<DocumentVersionInfo> getVersionsInfoByDocumentId(Long documentId);

    /**
     * Deletes all {@link DocumentVersion}, without extra logic (wrapper for repository method).
     *
     * @param documentId the ID of the {@link DocumentVersion}
     * */
    void deleteAllByDocumentId(@Param("documentId") Long documentId);
}
