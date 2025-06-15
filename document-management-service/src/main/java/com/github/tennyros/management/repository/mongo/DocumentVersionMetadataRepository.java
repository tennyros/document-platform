package com.github.tennyros.management.repository.mongo;

import com.github.tennyros.management.entity.DocumentVersionMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentVersionMetadataRepository extends MongoRepository<DocumentVersionMetadata, String> {

    Optional<DocumentVersionMetadata> findByDocumentVersionId(Long documentVersionId);

    void deleteByDocumentVersionId(Long documentVersionId);
}
