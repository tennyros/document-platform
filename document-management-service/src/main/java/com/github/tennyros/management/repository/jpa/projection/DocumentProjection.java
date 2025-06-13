package com.github.tennyros.management.repository.jpa.projection;

import com.github.tennyros.management.entity.Document;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDateTime;

@Projection(name = "documentView", types = Document.class)
public interface DocumentProjection {

    Long getId();
    String getTitle();
    String getDescription();
    LocalDateTime getCreatedAt();
}