package com.github.tennyros.management.model;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "document_audit")
public class DocumentAudit {

    @Id
    private String id;

    private Long documentId;

    private String action;

    private String username;

    private LocalDateTime timestamp;

    private Map<String, Object> data;

}
