package com.openAi.security.utils;

import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;

import java.security.Key;

public class JwtKeyGenerator {
    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Generates a secure random key
        String base64Key = Encoders.BASE64.encode(key.getEncoded()); // Encode key to Base64
        System.out.println("Generated Key: " + base64Key);
    }
}
