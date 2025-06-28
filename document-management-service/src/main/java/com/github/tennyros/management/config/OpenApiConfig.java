package com.github.tennyros.management.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${application.version}")
    private String appVersion;

    @Bean
    public OpenAPI documentManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Document Management API")
                        .version(appVersion)
                        .description("REST API for managing Documents, their Versions, Metadata, Signatures")
                );
    }
}
