package com.example.filemanager.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER_PREFIX = "Bearer_";

    private final JwtHandler jwtHandler;

    public BearerTokenServerAuthenticationConverter(JwtHandler jwtHandler) {
        this.jwtHandler = jwtHandler;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractBearerToken(exchange)
                .flatMap(getBearerValue)
                .flatMap(jwtHandler::checkToken)
                .flatMap(UserAuthenticationBearer::create);
    }

    private static final Function<String, Mono<String>> getBearerValue = authValue ->
            Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));

    private Mono<String> extractBearerToken(ServerWebExchange exchange) {
        return Mono.justOrEmpty(
                exchange.getRequest()
                        .getHeaders()
                        .getFirst(HttpHeaders.AUTHORIZATION)
        );
    }

}
