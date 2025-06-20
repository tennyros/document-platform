package com.github.tennyros.signing.exception;

public class SignatureProcessingException extends RuntimeException {

    public SignatureProcessingException(String message, Throwable e) {
        super(message, e);
    }
}
