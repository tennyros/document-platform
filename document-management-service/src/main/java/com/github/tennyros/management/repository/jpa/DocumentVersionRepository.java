package com.github.tennyros.management.repository;

import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.entity.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findByStorageKey(String storageKey);

    Optional<DocumentVersion> findTopByDocumentOrderByVersionNumberDesc(Document document);

    boolean existsByStorageKey(String storageKey);
}
