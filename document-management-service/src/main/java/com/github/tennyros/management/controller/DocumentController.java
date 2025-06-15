package com.github.tennyros.management.controller;

import com.github.tennyros.management.dto.document.request.DocumentFilter;
import com.github.tennyros.management.dto.document.request.DocumentUploadRequest;
import com.github.tennyros.management.dto.document.response.DocumentResponse;
import com.github.tennyros.management.dto.document.response.PageResponse;
import com.github.tennyros.management.entity.Document;
import com.github.tennyros.management.mapper.DocumentMapper;
import com.github.tennyros.management.mapper.PageMapper;
import com.github.tennyros.management.service.DocumentOrchestrationService;
import com.github.tennyros.management.service.DocumentService;
import com.github.tennyros.management.util.ResponseFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
public class DocumentController {

    private final DocumentOrchestrationService documentOrchestrationService;
    private final DocumentService documentService;

    private final PageMapper pageMapper;
    private final DocumentMapper documentMapper;

    @PostMapping
    public ResponseEntity<DocumentResponse> upload(@Valid @ModelAttribute DocumentUploadRequest metadata) {
        log.info("Uploading Document with name={}", metadata.getFile().getOriginalFilename());
        DocumentResponse response = documentOrchestrationService.uploadDocument(metadata);
        log.info("Upload completed successfully for Document with id={}", response.getId());
        return ResponseFactory.created(response.getId(), response);
    }

    @GetMapping
    public PageResponse<DocumentResponse> getFilteredDocuments(@Valid @ModelAttribute DocumentFilter filterDto,
                                                               Pageable pageable) {
        log.debug("Filtering Documents...");
        Page<Document> page = documentService.filterDocuments(filterDto, pageable);
        return pageMapper.toPageResponse(page, documentMapper::toDto);
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> delete(@PathVariable Long documentId) {
        log.info("Deleting Document with id={}", documentId);
        documentService.deleteDocument(documentId);
        log.info("Successfully deleted Document with id={}", documentId);
        return ResponseFactory.noContent();
    }
}
