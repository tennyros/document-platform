package com.github.tennyros.signing.service.impl;

import com.github.tennyros.dto.DocumentHashResponse;
import com.github.tennyros.dto.SaveSignatureRequest;
import com.github.tennyros.signing.client.DocumentManagementClient;
import com.github.tennyros.signing.dto.SignDocumentRequest;
import com.github.tennyros.signing.dto.SignDocumentResponse;
import com.github.tennyros.signing.service.SignatureService;
import com.github.tennyros.signing.service.SigningService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SigningServiceImpl implements SigningService {

    private final DocumentManagementClient documentManagementClient;
    private final SignatureService signatureService;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Override
    public SignDocumentResponse signDocument(SignDocumentRequest request) {
        DocumentHashResponse hashData = documentManagementClient.fetchHash(
                request.getDocumentId(), request.getVersionId());

        byte[] hashBytes = Base64.getDecoder().decode(hashData.getHash());
        byte[] signature = signatureService.signDocument(hashBytes, privateKey);

        SaveSignatureRequest signatureRequest = SaveSignatureRequest.builder()
                .hash(hashData.getHash())
                .signature(signature)
                .signatureAlgorithm("SHA256withRSA")
                .certificateSubject("CN=User, OU=Dev, O=Company, C=RU")
                .certificateIssuer("CN=CA, O=Authority, C=RU")
                .certificateSerialNumber("ABC123456789")
                .signedAt(LocalDateTime.now())
                .signedBy("username")
                .build();

        Long savedSignatureId = documentManagementClient.saveSignature(
                request.getDocumentId(), request.getVersionId(), signatureRequest);

        return SignDocumentResponse.builder()
                .documentId(request.getDocumentId())
                .signatureId(savedSignatureId)
                .signedAt(LocalDateTime.now())
                .signedBy("username")
                .build();
    }

    @Override
    public boolean verifySignature(String documentId, String signatureId) {
        byte[] document = documentManagementClient.getDocumentMetadata(documentId, signatureId);
        byte[] signature = documentManagementClient.getSignature(documentId);
        return signatureService.verifySignature(document, signature, publicKey);
    }
}
