package com.example.filemanager.service;

import com.example.filemanager.dto.AuthRequestDTO;
import com.example.filemanager.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUser(Long userId);

    Mono<UserDTO> createUser(UserDTO userDTO);

    Mono<UserDTO> createUser(AuthRequestDTO authRequest);

    Mono<Void> updateUser(Long userId, UserDTO userDTO);

    Mono<Void> deleteUser(Long userId);
}
