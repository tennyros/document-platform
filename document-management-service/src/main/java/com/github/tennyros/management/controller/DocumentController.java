package com.github.tennyros.management.controller;

import com.github.tennyros.management.advice.ErrorResponse;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/documents")
@Tag(name = "Documents", description = "Managing main Documents")
public class DocumentController {

    private final DocumentOrchestrationService documentOrchestrationService;
    private final DocumentService documentService;

    private final PageMapper pageMapper;
    private final DocumentMapper documentMapper;

    @Operation(
            summary = "Upload Document",
            description = "Upload new Document with and it's first Version and Metadata",
            requestBody = @RequestBody(content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE)),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Document successfully uploaded",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = DocumentResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Fields validation error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentResponse> upload(@Valid @ModelAttribute DocumentUploadRequest metadata) {
        log.info("Uploading Document with name={}", metadata.getFile().getOriginalFilename());
        DocumentResponse response = documentOrchestrationService.uploadDocument(metadata);
        log.info("Upload completed successfully for Document with id={}", response.getId());
        return ResponseFactory.created(response.getId(), response);
    }

    @Operation(
            summary = "Filtering Documents",
            description = "Retrieve Document Page with filtering and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Documents successfully retrieved",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PageResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid filter parameters",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @GetMapping
    public PageResponse<DocumentResponse> getFilteredDocuments(@Valid @ModelAttribute DocumentFilter filterDto,
                                                               Pageable pageable) {
        log.debug("Filtering Documents...");
        Page<Document> page = documentService.filterDocuments(filterDto, pageable);
        return pageMapper.toPageResponse(page, documentMapper::toDto);
    }

    @Operation(
            summary = "Filtering Documents",
            description = "Retrieve Document Page with filtering and pagination",
            responses = {
                    @ApiResponse(responseCode = "204",
                            description = "Document and it's Versions successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Document not found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Unexpected server error",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> delete(@PathVariable Long documentId) {
        log.info("Deleting Document with id={}", documentId);
        documentService.deleteDocument(documentId);
        log.info("Successfully deleted Document with id={}", documentId);
        return ResponseFactory.noContent();
    }
}
