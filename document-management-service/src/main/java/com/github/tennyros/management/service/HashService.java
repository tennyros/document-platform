package com.github.tennyros.management.service;

import java.io.InputStream;

/**
 * Service for calculating cryptographic hash values.
 */
public interface HashService {

    /**
     * Calculates the SHA-256 hash of the given input stream.
     *
     * @param inputStream input stream to hash (will be read entirely)
     * @return Base64-encoded SHA-256 hash string
     * @throws IllegalStateException if hash calculation fails
     */
    String calculateSHA256(InputStream inputStream);
}
