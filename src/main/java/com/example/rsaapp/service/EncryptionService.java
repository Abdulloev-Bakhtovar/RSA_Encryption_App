package com.example.rsaapp.service;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {
    private static final int BIT_LENGTH = 512;

    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(BIT_LENGTH, new SecureRandom());
        return keyGen.genKeyPair();
    }

    public byte[] encrypt(byte[] data, PublicKey pubKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    public byte[] decrypt(byte[] data, PrivateKey privKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(data);
    }

    public String bytesToDigits(byte[] data) {
        BigInteger bi = new BigInteger(1, data);
        return bi.toString();
    }

    public byte[] digitsToBytes(String digits) {
        BigInteger bi = new BigInteger(digits);
        return bi.toByteArray();
    }
}
