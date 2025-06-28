package com.github.tennyros.management.service.impl;

import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.management.entity.DocumentSignature;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.mapper.DocumentSignatureMapper;
import com.github.tennyros.management.repository.jpa.DocumentSignatureRepository;
import com.github.tennyros.management.service.DocumentSignatureService;
import com.github.tennyros.management.service.DocumentVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentSignatureServiceImpl implements DocumentSignatureService {

    private final DocumentVersionService documentVersionService;

    private final DocumentSignatureRepository documentSignatureRepository;

    private final DocumentSignatureMapper documentSignatureMapper;

    @Override
    @Transactional
    public Long saveSignature(Long documentId, Long versionId, SaveSignatureRequest request) {
        DocumentVersion version = documentVersionService.getDocumentVersion(documentId, versionId);

        if (!request.getHash().equals(version.getHash())) {
            throw new IllegalArgumentException("Signed hash does not match document version hash");
        }

        DocumentSignature signature = documentSignatureMapper.toEntity(request);
        signature.setDocumentVersion(version);

        DocumentSignature savedSignature = documentSignatureRepository.save(signature);
        log.info("Signature saved for Document with id={} and Version with id={}", documentId, versionId);
        return savedSignature.getId();
    }
}
