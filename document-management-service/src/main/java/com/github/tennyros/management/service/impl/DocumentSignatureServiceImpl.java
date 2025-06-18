package com.github.tennyros.management.service.impl;

import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.management.entity.DocumentSignature;
import com.github.tennyros.management.entity.DocumentVersion;
import com.github.tennyros.management.exception.DocumentVersionNotFoundException;
import com.github.tennyros.management.repository.jpa.DocumentSignatureRepository;
import com.github.tennyros.management.repository.jpa.DocumentVersionRepository;
import com.github.tennyros.management.service.DocumentSignatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DocumentSignatureServiceImpl implements DocumentSignatureService {

    private final DocumentVersionRepository documentVersionRepository;
    private final DocumentSignatureRepository documentSignatureRepository;

    @Override
    @Transactional
    public Long saveSignature(Long documentId, Long versionId, SaveSignatureRequest request) {
        DocumentVersion version = documentVersionRepository
                .findByDocumentIdAndVersionNumber(documentId, versionId)
                .orElseThrow(() -> new DocumentVersionNotFoundException(String.format(
                        "Version id %d of Document with id %d not found ", versionId, documentId)));

        if (!request.getHash().equals(version.getHash())) {
            throw new IllegalArgumentException("Signed hash does not match document version hash");
        }

        DocumentSignature signature = DocumentSignature.builder()
                .documentVersion(version)
                .signature(request.getSignature())
                .signatureAlgorithm(request.getSignatureAlgorithm())
                .certificateSubject(request.getCertificateSubject())
                .certificateIssuer(request.getCertificateIssuer())
                .certificateSerialNumber(request.getCertificateSerialNumber())
                .signedAt(request.getSignedAt())
                .signedBy(request.getSignedBy())
                .build();

        DocumentSignature savedSignature = documentSignatureRepository.save(signature);
        log.info("Signature saved for Document with id={} and Version with id={}", documentId, versionId);
        return savedSignature.getId();
    }
}
