package com.example.filemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    private ApplicationConfig() {
    }

    @Value("${jwt.password.encoder.iterations}")
    private static Integer iterations;

    @Value("${jwt.password.encoder.keyLength}")
    private static Integer keyLength;

    @Value("${jwt.password.encoder.secret}")
    private static String encoderSecret;

    @Value("${jwt.expiration}")
    private static Integer jwtExpiration;

    @Value("${jwt.issuer}")
    private static String jwtIssuer;

    @Value("${jwt.secret}")
    private static String jwtSecret;

    public static String getEncoderSecret() {
        return encoderSecret;
    }

    public static Integer getIterations() {
        return iterations;
    }

    public static Integer getKeyLength() {
        return keyLength;
    }

    public static Integer getJwtExpiration() {
        return jwtExpiration;
    }

    public static String getJwtIssuer() {
        return jwtIssuer;
    }

    public static String getJwtSecret() {
        return jwtSecret;
    }
}
