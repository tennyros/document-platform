package com.github.tennyros.signing.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignDocumentResponse {

    private Long documentId;
    private Long signatureId;
    private LocalDateTime signedAt;
    private String signedBy;
}
