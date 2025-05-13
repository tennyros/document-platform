package com.github.tennyros.management.exception;

public class DocumentUploadProcessException extends BaseInternalServerException {

    public DocumentUploadProcessException(String message) {
        super(message);
    }

    public DocumentUploadProcessException(String message, Throwable cause) {
        super(message, cause);
    }

}
