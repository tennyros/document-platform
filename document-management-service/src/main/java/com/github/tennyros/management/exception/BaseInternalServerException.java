package com.github.tennyros.management.exception;

public class BaseInternalServerException extends RuntimeException {

    public BaseInternalServerException(String message) {
        super(message);
    }

    public BaseInternalServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
