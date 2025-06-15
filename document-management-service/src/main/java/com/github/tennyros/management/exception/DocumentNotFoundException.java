package com.github.tennyros.management.exception;

public class DocumentNotFoundException extends BaseNotFoundException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
