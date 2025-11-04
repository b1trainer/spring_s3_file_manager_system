package com.example.filemanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${jwt.password.encoder.iterations}")
    private Integer iterations;

    @Value("${jwt.password.encoder.keyLength}")
    private Integer keyLength;

    @Value("${jwt.password.encoder.secret}")
    private String encoderSecret;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    public String getEncoderSecret() {
        return encoderSecret;
    }

    public Integer getIterations() {
        return iterations;
    }

    public Integer getKeyLength() {
        return keyLength;
    }

    public Integer getJwtExpiration() {
        return jwtExpiration;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }
}
