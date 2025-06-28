package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.MinioStorageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service for managing document entities and their lifecycle,
 * including creation, deletion, and filtering.
 */
public interface DocumentService {

    /**
     * Creates a new document entity with initial metadata and version.
     * Also delegates the creation of the first document version.
     *
     * @param request the DTO containing document metadata and file
     * @param objectName the storage key of the file saved in MinIO
     * @return the persisted {@link Document} entity
     *
     * @throws IllegalStateException if the file cannot be read for hash calculation
     */
    Document createDocument(DocumentUploadRequest request, String objectName);

    /**
     * Deletes the specified document along with all its versions,
     * associated metadata (in Mongo), and files stored in MinIO.
     *
     * @param documentId the ID of the document to delete
     *
     * @throws DocumentNotFoundException if the document with the given ID does not exist
     * @throws FileAccessDeniedException if deletion in MinIO is denied
     * @throws MinioStorageException if an error occurs while deleting files from MinIO
     */
    void deleteDocument(Long documentId);

    /**
     * Retrieves {@link Page} collection of {@link Document} entities.
     *
     * @param filterDto the DTO containing filtering parameters
     * @param pageable the {@link Pageable} interface for pagination information
     *
     * @return the {@link Page} collection of {@link Document} entities
     * */
    Page<Document> filterDocuments(DocumentFilter filterDto, Pageable pageable);
}
