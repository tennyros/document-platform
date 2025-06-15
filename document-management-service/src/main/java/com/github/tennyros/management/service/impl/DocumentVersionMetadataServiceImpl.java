package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.entity.DocumentVersionMetadata;
import com.github.tennyros.management.repository.mongo.DocumentVersionMetadataRepository;
import com.github.tennyros.management.service.DocumentVersionMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentVersionMetadataServiceImpl implements DocumentVersionMetadataService {

    private final DocumentVersionMetadataRepository metadataRepository;

    @Override
    public void saveMetadata(Long documentVersionId, Map<String, Object> attributes) {
        log.debug("Saving Metadata for Document Version with id={} to MongoDB", documentVersionId);
        DocumentVersionMetadata metadata = DocumentVersionMetadata.builder()
                .documentVersionId(documentVersionId)
                .attributes(attributes)
                .build();

        metadataRepository.save(metadata);
        log.debug("Successfully saved Metadata for Document Version with id={} to MongoDB", documentVersionId);
    }

    @Override
    public Optional<DocumentVersionMetadata> getMetadata(Long documentVersionId) {
        return metadataRepository.findByDocumentVersionId(documentVersionId);
    }

    @Override
    public void deleteMetadata(Long documentVersionId) {
        log.debug("Deleting Metadata for Document Version with id={} from MongoDB", documentVersionId);
        metadataRepository.findByDocumentVersionId(documentVersionId)
                .ifPresent(metadataRepository::delete);
        log.debug("Successfully deleted Metadata for Document Version with id={} from MongoDB", documentVersionId);
    }
}
