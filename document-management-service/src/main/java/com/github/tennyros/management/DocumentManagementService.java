package com.github.tennyros.management;

import com.github.tennyros.management.config.MinioProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MinioProperties.class)
public class DocumentManagementService {
    public static void main( String[] args ) {
        SpringApplication.run(DocumentManagementService.class, args);
    }
}
