package com.example.filemanager.controller;

import com.example.filemanager.config.UserRole;
import com.example.filemanager.dto.AuthRequestDTO;
import com.example.filemanager.dto.AuthResponseDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.exceptions.InvalidCredentialsException;
import com.example.filemanager.mapper.UserMapper;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.security.CustomPrincipal;
import com.example.filemanager.security.SecurityService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/v1/auth")
public class AuthController {

    private final SecurityService securityService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public AuthController(SecurityService securityService, UserMapper userMapper, UserRepository userRepository) {
        this.securityService = securityService;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @PostMapping("/signIn")
    public Mono<UserDTO> register(@RequestBody UserDTO userDTO) {
        if (userDTO.getUsername() == null ||
                userDTO.getUsername().isEmpty() ||
                userDTO.getPassword() == null ||
                userDTO.getPassword().isEmpty() ||
                userDTO.getRole() == UserRole.ADMIN
        ) {
            return Mono.error(new InvalidCredentialsException("Invalid credentials"));
        }

        UserEntity entity = userMapper.map(userDTO);

        return userRepository.save(entity)
                .map(userMapper::map);
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
    public Mono<UserDTO> getUserInfo(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return userRepository.findById(principal.getId())
                .map(userMapper::map);
    }
}
