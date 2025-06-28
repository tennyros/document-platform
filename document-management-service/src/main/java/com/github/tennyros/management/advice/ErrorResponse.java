package com.github.tennyros.management.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Standard error response structure for failed API requests.
 */
@Getter
@Setter
@Builder
public class ErrorResponse {

    /** Timestamp when the error occurred */
    private Instant timestamp;

    /** HTTP status code (e.g., 404, 500) */
    private int status;

    /** HTTP status reason (e.g., "Not Found", "Internal Server Error") */
    private String error;

    /** Human-readable error message */
    private String message;

    /** Request path that caused the error */
    private String path;

    /** Optional list of validation or field-specific errors */
    @JsonInclude(NON_NULL)
    private List<Map<String, Object>> errors;
}
