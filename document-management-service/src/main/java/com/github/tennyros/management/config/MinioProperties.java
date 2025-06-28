package com.github.tennyros.management.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * Configuration properties for MinIO client.
 * <p>Expected in application.yml:</p>
 * <pre>
 * minio:
 *   url: http://localhost:9000
 *   access-key: minioadmin
 *   secret-key: minioadmin
 *   bucket: my-bucket
 * </pre>
 */
@Slf4j
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    @NotBlank
    private String url;

    @NotBlank
    private String accessKey;

    @NotBlank
    private String secretKey;

    @NotBlank
    private String bucket;
}

