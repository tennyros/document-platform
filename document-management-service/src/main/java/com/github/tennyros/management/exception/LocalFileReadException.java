package com.github.tennyros.management.exception;

public class LocalFileReadException extends RuntimeException {

    public LocalFileReadException(String message) {
        super(message);
    }

    public LocalFileReadException(String message, Throwable cause) {
        super(message, cause);
    }

}
