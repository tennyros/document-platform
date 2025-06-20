package com.github.tennyros.signing.client;

import com.github.tennyros.dto.DocumentHashResponse;
import com.github.tennyros.dto.DocumentSignatureResponse;
import com.github.tennyros.dto.SaveSignatureRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class DocumentManagementClient {
    
    private final WebClient webClient;

    public DocumentManagementClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public DocumentHashResponse fetchHash(Long documentId, Long versionId) {
        return webClient.get()
                .uri("/api/v1/documents/{documentId}/versions/{versionId}/hash", documentId, versionId)
                .retrieve()
                .bodyToMono(DocumentHashResponse.class)
                .block(); // или .toFuture().get() если синхронно без блокировки
    }

    public Long saveSignature(Long documentId, Long versionId, SaveSignatureRequest request) {
        return Objects.requireNonNull(webClient.post()
                        .uri("/api/v1/documents/{documentId}/versions/{versionId}/signatures", documentId, versionId)
                        .bodyValue(request)
                        .retrieve()
                        .bodyToMono(DocumentSignatureResponse.class)
                        .block())
                .getId();
    }

    public byte[] getDocumentMetadata(String documentId, String versionId) {
        return webClient.get()
                .uri("/api/v1/documents/{documentId}/versions/{versionId}", documentId, versionId)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

    public byte[] getSignature(String documentId) {
        return webClient.get()
                .uri("/api/v1/documents/{documentId}/signatures", documentId)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}