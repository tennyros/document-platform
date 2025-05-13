package com.github.tennyros.management.exception;

public class FileAccessDeniedException extends RuntimeException {

    public FileAccessDeniedException(String message) {
        super(message);
    }

    public FileAccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }

}
