package com.github.tennyros.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.github.tennyros.management.repository.jpa")
@EnableMongoRepositories(basePackages = "com.github.tennyros.management.repository.mongo")
public class RepositoryConfig {
}