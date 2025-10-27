package com.example.filemanager.service.impl;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Mono<UserDTO> getUser(String id) {
        return null;
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<UserDTO> updateUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteUser(String id) {
        return null;
    }
}
