package com.github.tennyros.management.advice;

import com.github.tennyros.management.exception.BaseInternalServerException;
import com.github.tennyros.management.exception.DocumentNotFoundException;
import com.github.tennyros.management.exception.DocumentUploadProcessException;
import com.github.tennyros.management.exception.FileAccessDeniedException;
import com.github.tennyros.management.exception.FileUploadException;
import com.github.tennyros.management.exception.LocalFileReadException;
import com.github.tennyros.management.exception.MinioStorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(DocumentNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleFileNotFound(DocumentNotFoundException ex,
                                                               HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(FileAccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleFileAccessDenied(FileAccessDeniedException ex,
                                                                   HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(FileUploadException.class)
    protected ResponseEntity<ErrorResponse> handleFileUpload(FileUploadException ex,
                                                             HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({LocalFileReadException.class, MinioStorageException.class,
            DocumentUploadProcessException.class})
    protected ResponseEntity<ErrorResponse> handleLocalFileRead(BaseInternalServerException ex,
                                                                HttpServletRequest request) {
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleOtherExceptions(Exception ex, HttpServletRequest request) {
        ErrorResponse error = buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request,
                null
        );

        log.error("Exception: {} - Request URI: {} - Method: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleValidationExceptions(BindException ex,
                                                                       HttpServletRequest request) {
        List<Map<String, Object>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        ErrorResponse error = buildErrorResponse(HttpStatus.BAD_REQUEST,
                "Validation failed for one or more fields", request, fieldErrors);

        log.warn("Validation failed: {}", fieldErrors);
        return ResponseEntity.badRequest().body(error);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status,
                                                             HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        log.error("Exception: {} - Request URI: {} - Method: {}",
                ex.getClass().getSimpleName(),
                request.getRequestURI(),
                request.getMethod(), ex);

        return ResponseEntity.status(status).body(error);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message,
                                             HttpServletRequest request,
                                             List<Map<String, Object>> errors) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .errors(errors)
                .build();
    }

    private Map<String, Object> mapFieldError(FieldError fieldError) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("field", fieldError.getField());
        errorDetails.put("message", fieldError.getDefaultMessage());

        Object rejectedValue = fieldError.getRejectedValue();
        if (rejectedValue instanceof MultipartFile) {
            errorDetails.put("rejectedValue", ((MultipartFile) rejectedValue).getOriginalFilename());
        } else {
            errorDetails.put("rejectedValue", rejectedValue);
        }

        return errorDetails;
    }

}
