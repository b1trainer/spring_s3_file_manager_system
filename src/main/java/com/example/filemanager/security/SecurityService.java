package com.example.filemanager.security;

import com.example.filemanager.config.ApplicationConfig;
import com.example.filemanager.config.status.UserStatus;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class SecurityService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (user.getStatus().equals(UserStatus.DELETED)) {
                        return Mono.error(new UsernameNotFoundException("Username not found"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new UsernameNotFoundException("Invalid password"));
                    }

                    return Mono.just(createToken(user));
                })
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("Username not found")));
    }

    private TokenDetails createToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("username", user.getUsername());

        return createToken(claims, user.getId().toString());
    }

    private TokenDetails createToken(Map<String, Object> claims, String subjectId) {
        long expirationTimeInMillis = ApplicationConfig.getJwtExpiration() * 1000L;
        Date expirationAt = new Date(expirationTimeInMillis);

        return createToken(expirationAt, claims, subjectId);
    }

    private TokenDetails createToken(Date expirationAt, Map<String, Object> claims, String subjectId) {
        Date createdAt = new Date();

        String token = Jwts.builder()
                .claims(claims)
                .issuer(ApplicationConfig.getJwtIssuer())
                .subject(subjectId)
                .issuedAt(new Date())
                .id(UUID.randomUUID().toString())
                .expiration(expirationAt)
                .signWith(Keys.hmacShaKeyFor(ApplicationConfig.getEncoderSecret().getBytes(StandardCharsets.UTF_8)))
                .compact();

        TokenDetails details = new TokenDetails();
        details.setToken(token);
        details.setExpiresAt(expirationAt);
        details.setUserid(Long.parseLong(subjectId));
        details.setIssuedAt(createdAt);

        return details;
    }

}
