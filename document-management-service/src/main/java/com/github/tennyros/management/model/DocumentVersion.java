package com.github.tennyros.management.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "document_version")
public class DocumentVersion {

    @Id
    @GeneratedValue
    private Long id;

    private String filename;

    private String contentType;

    private long size;

    private String storageKey;

    private String versionLabel;

    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

}