package org.example.jiaoji.security;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.lang.util.ByteSource;

import java.security.SecureRandom;
import java.util.Base64;

public class PasswordEncoder {
    static public String encode(String password, String salt) {
        int iterations = 1000;
        Sha256Hash hash=new Sha256Hash(password, ByteSource.Util.bytes(salt), iterations);
        return hash.toBase64();
    }

    static public String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}