package com.github.tennyros.management.dto.version.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionResponse {

    private Long id;
    private Long documentId;
    private long versionNumber;
    private String filename;
    private String contentType;
    private long size;
    private String storageKey;
    private LocalDateTime uploadedAt;
    private String hash;
}
