package com.github.tennyros.management.config;

import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.SpringDataMongoV3Driver;
import com.github.cloudyrock.spring.v5.MongockSpring5;
import com.github.cloudyrock.spring.v5.MongockSpring5.MongockInitializingBeanRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Mongock migrations config for mongoDB entity
 * */
@Configuration
public class MongockConfiguration {

    @Bean
    public MongockInitializingBeanRunner mongockInitializingBeanRunner(MongoTemplate mongoTemplate, ApplicationContext springContext) {
        SpringDataMongoV3Driver driver = SpringDataMongoV3Driver.withDefaultLock(mongoTemplate);

        return MongockSpring5.builder()
                .setDriver(driver)
                .addChangeLogsScanPackage("com.github.tennyros.management.migration.mongo")
                .setSpringContext(springContext)
                .buildInitializingBeanRunner();
    }
}