package com.github.tennyros.management.repository;

import com.github.tennyros.management.model.DocumentVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentVersionRepository extends JpaRepository<DocumentVersion, Long> {

    Optional<DocumentVersion> findByStorageKey(String storageKey);

    boolean existsByStorageKey(String storageKey);
}
