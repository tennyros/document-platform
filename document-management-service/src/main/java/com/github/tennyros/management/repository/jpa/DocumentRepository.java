package com.github.tennyros.management.repository.jpa;

import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.repository.jpa.projection.DocumentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "documents", excerptProjection = DocumentProjection.class)
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {

}
