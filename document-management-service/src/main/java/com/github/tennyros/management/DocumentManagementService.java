package com.github.tennyros.management;

import com.github.tennyros.management.config.MinioProperties;
import com.github.tennyros.management.util.DotenvLoader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(MinioProperties.class)
public class DocumentManagementService {
    public static void main( String[] args ) {
        DotenvLoader.load();
        SpringApplication.run(DocumentManagementService.class, args);
    }
}
