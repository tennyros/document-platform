package com.github.tennyros.management.service;

import com.github.tennyros.management.config.MinioProperties;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.FileNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public String upload(MultipartFile file) {
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(minioProperties.getBucket())
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return objectName;
        } catch (ErrorResponseException e) {
            throw new FileAccessDeniedException("Ошибка доступа к bucket", e);
        } catch (InsufficientDataException e) {
            throw new FileUploadException("Недостаточно данных для загрузки", e);
        } catch (IOException e) {
            throw new LocalFileReadException("Невозможно прочитать файл", e);
        } catch (Exception e) {
            throw new MinioStorageException("Ошибка при загрузке файла", e);
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
                throw new FileNotFoundException("Файл не найден: " + objectName, e);
            } else if ("AccessDenied".equals(code)) {
                throw new FileAccessDeniedException("Нет доступа к файлу: " + objectName, e);
            }
            throw new MinioStorageException("MinIO ошибка: " + code, e);
        } catch (Exception e) {
            throw new MinioStorageException("Ошибка при скачивании файла: " + objectName, e);
        }
    }

    public void delete(String objectName) {
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
                throw new FileNotFoundException("Файл не найден: " + objectName);
            } else if ("AccessDenied".equals(code)) {
                throw new FileAccessDeniedException("Нет доступа к удалению: " + objectName);
            }
            throw new MinioStorageException("MinIO ошибка: " + code, e);
        } catch (Exception e) {
            throw new MinioStorageException("Ошибка при удалении файла: " + objectName, e);
        }
    }
}
