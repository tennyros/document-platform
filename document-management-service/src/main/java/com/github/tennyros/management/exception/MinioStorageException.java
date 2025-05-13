package com.github.tennyros.management.exception;

public class MinioStorageException extends BaseInternalServerException {

    public MinioStorageException(String message) {
        super(message);
    }

    public MinioStorageException(String message, Throwable cause) {
        super(message, cause);
    }

}
