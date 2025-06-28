package com.github.tennyros.management.service;

import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.document.response.DocumentResponse;
import com.github.tennyros.management.dto.version.response.DocumentFileDto;
import com.github.tennyros.management.dto.version.response.DocumentVersionResponse;
import com.github.tennyros.management.dto.version.request.DocumentVersionUploadRequest;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentUploadProcessException;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.LocalFileReadException;
import com.github.tennyros.management.exception.MinioStorageException;

/**
 * High-level service responsible for orchestrating document and version operations,
 * including storage (MinIO), metadata persistence, and retrieval.
 */
public interface DocumentOrchestrationService {

    /**
     * Uploads a new document, including saving the file to MinIO and persisting metadata.
     *
     * @param request the upload request containing file and metadata
     * @return the response DTO containing document info
     * @throws DocumentUploadProcessException if any error occurs during upload or metadata persistence
     * @throws FileAccessDeniedException if MinIO access is denied
     * @throws LocalFileReadException if file cannot be read
     * @throws MinioStorageException if MinIO operation fails
     */
    DocumentResponse uploadDocument(DocumentUploadRequest request);

    /**
     * Uploads a new version for an existing document, saving the file and metadata.
     *
     * @param documentId the ID of the existing document
     * @param request the upload request containing new version's file
     * @return the response DTO containing version info
     * @throws DocumentNotFoundException if the document does not exist
     * @throws DocumentUploadProcessException if any error occurs during upload or metadata persistence
     * @throws FileAccessDeniedException if MinIO access is denied
     * @throws LocalFileReadException if file cannot be read
     * @throws MinioStorageException if MinIO operation fails
     */
    DocumentVersionResponse uploadDocumentVersion(Long documentId, DocumentVersionUploadRequest request);

    /**
     * Downloads a specific version of a document from MinIO and returns its content and metadata.
     *
     * @param documentId the ID of the parent document
     * @param versionNumber the version number to download
     * @return the DTO containing file content and metadata
     * @throws DocumentNotFoundException if the document or version does not exist
     * @throws FileAccessDeniedException if MinIO access is denied
     * @throws MinioStorageException if file retrieval fails
     */
    DocumentFileDto downloadDocumentVersion(Long documentId, Long versionNumber);
}
