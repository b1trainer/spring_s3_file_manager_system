package com.example.filemanager.config;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.security.AuthenticationManager;
import com.example.filemanager.security.BearerTokenServerAuthenticationConverter;
import com.example.filemanager.security.JwtHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final String[] NO_AUTH_ROUTES = {"/rest/v1/auth/signIn", "/rest/v1/auth/logIn"};
    private final String[] MODERATOR_ROUTES = {"/rest/v1/files/**", "/rest/v1/events/**", "/rest/v1/users/**"};
    private final String[] DEFAULT_USER_ROUTES = {"/rest/v1/files/**"};

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange ->
                        exchange
                                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                                .pathMatchers(NO_AUTH_ROUTES).permitAll()
                                .pathMatchers("/**").hasRole(UserDTO.UserRole.ADMIN.getRoleName())
                                .pathMatchers(MODERATOR_ROUTES).hasRole(UserDTO.UserRole.MODERATOR.getRoleName())
                                .pathMatchers(DEFAULT_USER_ROUTES).hasRole(UserDTO.UserRole.USER.getRoleName())
                                .pathMatchers("/rest/v1/auth/info").authenticated()
                                .anyExchange().authenticated()
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint((swe, authException) -> Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)))
                                .accessDeniedHandler((swe, authException) -> Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)))

                )
                .addFilterAt(bearerAuthFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter bearerAuthFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter authFilter = new AuthenticationWebFilter(authenticationManager);
        authFilter.setServerAuthenticationConverter(
                new BearerTokenServerAuthenticationConverter(new JwtHandler())
        );
        authFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return authFilter;
    }
}
