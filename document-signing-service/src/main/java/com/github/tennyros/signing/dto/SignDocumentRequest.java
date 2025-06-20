package com.github.tennyros.signing.dto;

import com.github.tennyros.signing.enums.SignatureType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignDocumentRequest {

    @NotNull(message = "Document id is required")
    @Min(value = 1, message = "Document id must be positive")
    private Long documentId;

    @NotNull(message = "Document Version id is required")
    @Min(value = 1, message = "Document Version id must be positive")
    private Long versionId;

//    @NotBlank
    private SignatureType signatureType;
}
