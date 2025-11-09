package com.example.filemanager.security;

import com.example.filemanager.config.status.UserStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.util.Date;

public class JwtHandler {

    public JwtHandler() {
    }

    public static class Verification {
        public Claims claims;
        public String token;

        public Verification(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }

    public Mono<Verification> checkToken(String token) {
        return Mono.just(verify(token))
                .onErrorMap(e -> new RuntimeException("Token verification failed"));
    }

    private Verification verify(String token) {
        Claims claims = parseClaims(token);

        final Date expiry = claims.getExpiration();

        if (expiry.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        final String status = claims.get("status", String.class);

        if (status.equals(UserStatus.BLOCKED.getStatusValue())) {
            throw new RuntimeException("User is blocked");
        }

        return new Verification(claims, token);
    }

    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(generateRandomSHA256Secret()))
                .build();

        return parser.parseSignedClaims(token).getPayload();
    }

    private byte[] generateRandomSHA256Secret() {
        byte[] secret = new byte[32];
        new SecureRandom().nextBytes(secret);
        return secret;
    }
}
