package com.github.tennyros.management.repository;

import com.github.tennyros.management.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByTitle(String title);

    List<Document> findByTitleContainingIgnoreCase(String title);

    List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
