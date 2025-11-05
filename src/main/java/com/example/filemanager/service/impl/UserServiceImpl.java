package com.example.filemanager.service.impl;

import com.example.filemanager.config.UserRole;
import com.example.filemanager.config.status.UserStatus;
import com.example.filemanager.dto.AuthRequestDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.mapper.UserMapper;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Mono<UserDTO> getUser(Long userId) {
        return userRepository.findById(userId).flatMap(userEntity -> {
            UserDTO userDTO = userMapper.map(userEntity);
            return Mono.just(userDTO);
        });
    }

    @Override
    @Transactional
    public Mono<UserDTO> createUser(UserDTO userDTO) {
        UserEntity userEntity = userMapper.map(userDTO);
        return userRepository.save(userEntity).map(userMapper::map);
    }

    @Override
    public Mono<UserDTO> createUser(AuthRequestDTO authRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(authRequest.getUsername());
        userEntity.setPassword(authRequest.getPassword());
        userEntity.setRole(UserRole.USER);
        userEntity.setStatus(UserStatus.ACTIVE);

        return userRepository.save(userEntity).map(userMapper::map);
    }

    @Override
    @Transactional
    public Mono<Void> updateUser(Long userId, UserDTO userDTO) {
        return userRepository.findById(userId)
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
    @Transactional
    public Mono<Void> deleteUser(Long userId) {
        return userRepository.deleteById(userId);
    }
}
