package com.github.tennyros.management.service;

import com.github.tennyros.management.entity.DocumentVersionMetadata;

import java.util.Map;
import java.util.Optional;

public interface DocumentVersionMetadataService {

    void saveMetadata(Long documentVersionId, Map<String, Object> attributes);

    Optional<DocumentVersionMetadata> getMetadata(Long documentVersionId);

    void deleteMetadata(Long documentVersionId);
}