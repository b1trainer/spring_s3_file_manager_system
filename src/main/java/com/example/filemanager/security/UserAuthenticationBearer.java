package com.example.filemanager.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

public class UserAuthenticationBearer {

    public static Mono<Authentication> create(JwtHandler.Verification verification) {
        Claims claims = verification.claims;

        String subject = claims.getSubject();
        Long principalId = Long.parseLong(subject);

        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        String status = claims.get("status", String.class);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
        CustomPrincipal principal = new CustomPrincipal(principalId, username, status);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}
