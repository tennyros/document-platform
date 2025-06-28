package com.github.tennyros.management.service;

import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.FileUploadException;
import com.github.tennyros.management.exception.LocalFileReadException;
import com.github.tennyros.management.exception.MinioStorageException;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * Service for storing and retrieving files from MinIO object storage.
 */
public interface MinioService {

    /**
     * Uploads a file to MinIO storage and returns the generated storage key.
     *
     * @param file Multipart file to upload
     * @return unique object name used for storage
     * @throws FileAccessDeniedException if access to the bucket is denied
     * @throws FileUploadException if file can't be uploaded due to validation or I/O issues
     * @throws LocalFileReadException if file can't be read
     * @throws MinioStorageException for other unexpected MinIO errors
     */
    String upload(MultipartFile file);

    /**
     * Downloads a file from MinIO by its storage key.
     *
     * @param objectName storage key (filename) to retrieve
     * @return input stream of the file content
     * @throws DocumentNotFoundException if the file does not exist
     * @throws FileAccessDeniedException if access is denied
     * @throws MinioStorageException for other unexpected errors
     */
    InputStream download(String objectName);

    /**
     * Deletes a file from MinIO by its storage key.
     *
     * @param objectName storage key of the file to delete
     * @throws DocumentNotFoundException if file not found
     * @throws FileAccessDeniedException if deletion is denied
     * @throws MinioStorageException for unexpected MinIO errors
     */
    void delete(String objectName);
}
