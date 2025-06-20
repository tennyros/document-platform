package com.github.tennyros.signing.service.impl;

import com.github.tennyros.signing.service.SignatureService;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SignatureServiceImplTest {

    private final SignatureService signatureService = new SignatureServiceImpl();

    @Test
    void testSignAndVerify() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        byte[] data = "test document".getBytes();

        byte[] signature = signatureService.signDocument(data, keyPair.getPrivate());

        assertTrue(signatureService.verifySignature(data, signature, keyPair.getPublic()));
    }
}
