package com.github.tennyros.management.service.impl;

import com.github.tennyros.management.service.HashService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class HashServiceImpl implements HashService {

    @Override
    public String calculateSHA256(InputStream inputStream) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            byte[] hashBytes = digest.digest();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new IllegalStateException("Failed to calculate SHA-256 hash", e);
        }
    }
}