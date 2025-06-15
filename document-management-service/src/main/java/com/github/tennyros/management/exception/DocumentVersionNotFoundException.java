package com.github.tennyros.management.exception;

public class DocumentVersionNotFoundException extends BaseNotFoundException {

    public DocumentVersionNotFoundException(String format) {
        super(format);
    }

    public DocumentVersionNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
