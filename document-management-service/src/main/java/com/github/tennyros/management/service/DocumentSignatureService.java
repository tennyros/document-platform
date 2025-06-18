package com.github.tennyros.management.service;

import com.github.tennyros.dto.SaveSignatureRequest;

public interface DocumentSignatureService {

    Long saveSignature(Long documentId, Long versionId, SaveSignatureRequest request);
}
