package com.github.tennyros.signing.service.impl;

import com.github.tennyros.signing.exception.SignatureProcessingException;
import com.github.tennyros.signing.service.SignatureService;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

@Service
public class SignatureServiceImpl implements SignatureService {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    @Override
    public byte[] signDocument(byte[] document, PrivateKey privateKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
            signature.initSign(privateKey);
            signature.update(document);
            return signature.sign();
        } catch (GeneralSecurityException e) {
            throw new SignatureProcessingException("Error during signing", e);
        }
    }

    @Override
    public boolean verifySignature(byte[] document, byte[] signatureBytes, PublicKey publicKey) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
            signature.initVerify(publicKey);
            signature.update(document);
            return signature.verify(signatureBytes);
        } catch (GeneralSecurityException e) {
            throw new SignatureProcessingException("Failed to verify signature", e);
        }
    }
}
