package com.github.tennyros.management.service;

import com.github.tennyros.management.config.MinioProperties;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.FileUploadException;
import com.github.tennyros.management.exception.LocalFileReadException;
import com.github.tennyros.management.exception.MinioStorageException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file) {
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        log.info("Saving {} file to MinIO server", objectName);
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            log.info("Successfully saved {} file to MinIO server", objectName);
            return objectName;
        } catch (ErrorResponseException e) {
            log.error("Access denied when uploading {}: {}", objectName, e.getMessage(), e);
            throw new FileAccessDeniedException("Access to bucket denied", e);
        } catch (InsufficientDataException e) {
            log.error("Insufficient data when uploading {}: {}", objectName, e.getMessage(), e);
            throw new FileUploadException("Insufficient data for upload", e);
        } catch (IOException e) {
            log.error("I/O error when uploading {}: {}", objectName, e.getMessage(), e);
            throw new LocalFileReadException("Unable to read file", e);
        } catch (Exception e) {
            log.error("Unexpected error when uploading {}: {}", objectName, e.getMessage(), e);
            throw new MinioStorageException("Error during file upload", e);
        }
    }

    public InputStream download(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            String code = e.errorResponse().code();
            if ("NoSuchKey".equals(code)) {
                throw new DocumentNotFoundException("File not found: " + objectName, e);
            } else if ("AccessDenied".equals(code)) {
                log.error("Access denied when downloading {}: {}", objectName, e.getMessage(), e);
                throw new FileAccessDeniedException("Access to file denied: " + objectName, e);
            }
            log.error("Minio error response when downloading {}: {}", objectName, e.getMessage(), e);
            throw new MinioStorageException("MinIO error: " + code, e);
        } catch (Exception e) {
            log.error("Unexpected error when downloading {}: {}", objectName, e.getMessage(), e);
            throw new MinioStorageException("Error during file download: " + objectName, e);
        }
    }

    public void delete(String objectName) {
        log.info("Deleting document from MinIO server");
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .build()
            );
        } catch (ErrorResponseException e) {
            String code = e.errorResponse().code();
            if ("NoSuchKey".equals(code)) {
                throw new DocumentNotFoundException("File not found: " + objectName, e);
            } else if ("AccessDenied".equals(code)) {
                throw new FileAccessDeniedException("No access to delete: " + objectName, e);
            }
            throw new MinioStorageException("MinIO error: " + code, e);
        } catch (Exception e) {
            throw new MinioStorageException("Error during file deletion: " + objectName, e);
        }
    }
}
