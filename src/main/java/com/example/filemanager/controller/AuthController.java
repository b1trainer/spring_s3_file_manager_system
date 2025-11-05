package com.example.filemanager.controller;

import com.example.filemanager.dto.AuthRequestDTO;
import com.example.filemanager.dto.AuthResponseDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.exceptions.InvalidCredentialsException;
import com.example.filemanager.security.CustomPrincipal;
import com.example.filemanager.security.SecurityService;
import com.example.filemanager.service.UserService;
import com.example.filemanager.utils.Validation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/v1/auth")
public class AuthController {

    private final SecurityService securityService;
    private final UserService userService;

    public AuthController(SecurityService securityService, UserService userService) {
        this.securityService = securityService;
        this.userService = userService;
    }

    @PostMapping("/signIn")
    public Mono<ResponseEntity<UserDTO>> register(@RequestBody AuthRequestDTO authDTO) {
        if (!Validation.isCredentialsValid(authDTO)) {
            return Mono.error(new InvalidCredentialsException());
        }

        return userService.createUser(authDTO)
                .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PostMapping("/logIn")
    public Mono<AuthResponseDTO> login(@RequestBody AuthRequestDTO authRequestDTO) {
        return securityService.authenticate(authRequestDTO.getUsername(), authRequestDTO.getPassword())
                .flatMap(tokenDetails -> {
                    AuthResponseDTO authResponseDTO = new AuthResponseDTO();
                    authResponseDTO.setUserid(tokenDetails.getUserid());
                    authResponseDTO.setToken(tokenDetails.getToken());
                    authResponseDTO.setExpireAt(tokenDetails.getExpiresAt());
                    authResponseDTO.setIssuedAt(tokenDetails.getIssuedAt());
                    return Mono.just(authResponseDTO);
                });
    }

    @GetMapping("/info")
    public Mono<ResponseEntity<UserDTO>> getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userService.getUser(principal.getId())
                .map(ResponseEntity::ok);
    }
}
