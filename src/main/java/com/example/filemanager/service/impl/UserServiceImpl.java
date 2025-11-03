package com.example.filemanager.service.impl;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.mapper.UserMapper;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public Mono<UserDTO> getUser(String userId) {
        Long id = Long.parseLong(userId);

        return userRepository.findById(id).flatMap(userEntity -> {
            UserDTO userDTO = userMapper.map(userEntity);
            return Mono.just(userDTO);
        });
    }

    @Override
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public Mono<Void> updateUser(String userId, UserDTO userDTO) {
        Long id = Long.parseLong(userId);

        return userRepository.findById(id)
                .flatMap(userEntity -> {
                    if (userDTO.getUsername() != null) {
                        userEntity.setUsername(userDTO.getUsername());
                    }

                    if (userDTO.getPassword() != null) {
                        userEntity.setPassword(userDTO.getPassword());
                    }

                    if (userDTO.getStatus() != null) {
                        userEntity.setStatus(userDTO.getStatus());
                    }

                    if (userDTO.getRole() != null) {
                        userEntity.setRole(userDTO.getRole());
                    }

                    return userRepository.save(userEntity);
                }).then();
    }

    @Override
    public Mono<Void> deleteUser(String userId) {
        Long id = Long.parseLong(userId);

        return userRepository.deleteById(id);
    }
}
