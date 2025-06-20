package com.github.tennyros.signing.service;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface SignatureService {

    byte[] signDocument(byte[] document, PrivateKey privateKey);

    boolean verifySignature(byte[] document, byte[] signature, PublicKey publicKey);
}
