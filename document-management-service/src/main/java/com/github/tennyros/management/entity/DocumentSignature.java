package com.github.tennyros.management.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_signature")
public class DocumentSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "document_version_id")
    private DocumentVersion documentVersion;

    @Lob
    private byte[] signature;

    private String signatureAlgorithm;
    private String certificateSubject;
    private String certificateIssuer;
    private String certificateSerialNumber;
    private LocalDateTime signedAt;
    private String signedBy;
}