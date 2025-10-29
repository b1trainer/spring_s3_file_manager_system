package com.example.filemanager.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtHandler {

    private final String secret;

    public JwtHandler(String secret) {
        this.secret = secret;
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
                .onErrorResume(e -> Mono.error(new RuntimeException("Token verification failed")));
    }

    private Verification verify(String token) {
        Claims claims = parseClaims(token);
        final Date expiry = claims.getExpiration();

        if (expiry.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new Verification(claims, token);
    }

    private Claims parseClaims(String token) {
        JwtParser parser = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build();

        return parser.parseSignedClaims(token).getPayload();
    }
}
