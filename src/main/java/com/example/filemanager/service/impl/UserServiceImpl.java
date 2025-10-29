package com.example.filemanager.service.impl;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    //1:20:33
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

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
