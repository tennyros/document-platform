package com.github.tennyros.management.repository.jpa;

import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "document-versions", collectionResourceRel = "documentVersions")
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findTopByDocumentOrderByVersionNumberDesc(Document document);

    Optional<DocumentVersion> findByDocumentIdAndVersionNumber(Long documentId, Long versionNumber);
}
