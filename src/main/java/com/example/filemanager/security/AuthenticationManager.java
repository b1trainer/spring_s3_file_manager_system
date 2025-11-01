package com.example.filemanager.security;

import com.example.filemanager.config.status.UserStatus;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.repository.UserRepository;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserRepository userRepository;

    public AuthenticationManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userRepository.findById(principal.getId())
                .filter(u -> !u.getStatus().equals(UserStatus.BLOCKED))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User not found")))
                .map(u -> authentication);
    }
}
