package com.github.tennyros.management.repository;

import com.github.tennyros.management.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByTitleContainingIgnoreCase(String title);
    List<Document> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

}
