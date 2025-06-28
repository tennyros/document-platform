package com.github.tennyros.management.config;

import com.github.tennyros.management.exception.MinioBucketCreationException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for initializing MinIO client and ensuring bucket exists.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class MinioConfig {

    private final MinioProperties properties;

    @Bean
    public MinioClient minioClient() {

        MinioClient client = MinioClient.builder()
                .endpoint(properties.getUrl())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();

        try {
            boolean exists = client.bucketExists(
                    BucketExistsArgs.builder()
                            .bucket(properties.getBucket())
                            .build()
            );

            if (!exists) {
                client.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(properties.getBucket())
                                .build()
                );
                log.info("MinIO bucket '{}' created", properties.getBucket());
            } else {
                log.info("MinIO bucket '{}' already exists", properties.getBucket());
            }
        } catch (Exception e) {
            log.error("Failed to verify or create MinIO bucket '{}'", properties.getBucket(), e);
            throw new MinioBucketCreationException("MinIO bucket init failed", e);
        }

        return client;
    }
}
