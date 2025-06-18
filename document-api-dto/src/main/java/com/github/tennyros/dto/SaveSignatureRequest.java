package com.github.tennyros.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveSignatureRequest {

    @NotBlank
    private String hash;

    @NotNull
    @Size(min = 1, message = "Signature must not be empty")
    private byte[] signature;

    @NotBlank
    private String signatureAlgorithm;

    @NotBlank
    private String certificateSubject;

    @NotBlank
    private String certificateIssuer;

    @NotBlank
    private String certificateSerialNumber;

    @NotNull
    private LocalDateTime signedAt;

    @NotBlank
    private String signedBy;
}