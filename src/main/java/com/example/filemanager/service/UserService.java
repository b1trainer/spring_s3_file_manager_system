package com.example.filemanager.service;

import com.example.filemanager.dto.UserDTO;
import reactor.core.publisher.Mono;

public interface UserService {
    public Mono<UserDTO> getUser(String id);

    public Mono<UserDTO> createUser(UserDTO userDTO);

    public Mono<UserDTO> updateUser(String userId, UserDTO userDTO);

    public Mono<Void> deleteUser(String id);
}
