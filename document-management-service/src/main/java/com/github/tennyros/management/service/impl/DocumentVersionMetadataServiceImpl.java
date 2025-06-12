package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.entity.DocumentVersionMetadata;
import com.github.tennyros.management.repository.mongo.DocumentVersionMetadataRepository;
import com.github.tennyros.management.service.DocumentVersionMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentVersionMetadataServiceImpl implements DocumentVersionMetadataService {

    private final DocumentVersionMetadataRepository metadataRepository;

    @Override
    public void saveMetadata(Long documentVersionId, Map<String, Object> attributes) {
        DocumentVersionMetadata metadata = DocumentVersionMetadata.builder()
                .documentVersionId(documentVersionId)
                .attributes(attributes)
                .build();

        metadataRepository.save(metadata);
    }

    @Override
    public Optional<DocumentVersionMetadata> getMetadata(Long documentVersionId) {
        return metadataRepository.findByDocumentVersionId(documentVersionId);
    }

    @Override
    public void deleteMetadata(Long documentVersionId) {
        metadataRepository.findByDocumentVersionId(documentVersionId)
                .ifPresent(metadataRepository::delete);
    }
}
