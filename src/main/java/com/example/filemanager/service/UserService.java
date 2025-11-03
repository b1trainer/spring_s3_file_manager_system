package com.example.filemanager.service;

import com.example.filemanager.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDTO> getUser(String id);

    Mono<UserDTO> createUser(UserDTO userDTO);

    Mono<Void> updateUser(String userId, UserDTO userDTO);

    Mono<Void> deleteUser(String id);
}
