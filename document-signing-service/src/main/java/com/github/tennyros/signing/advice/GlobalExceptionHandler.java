package com.github.tennyros.signing.advice;

import com.github.tennyros.signing.exception.SignatureProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SignatureProcessingException.class)
    public ResponseEntity<String> handleSignatureException(SignatureProcessingException ex) {
        log.error("Failed to sign/verify", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}