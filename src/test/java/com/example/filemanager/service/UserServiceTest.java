package com.example.filemanager.service;

import com.example.filemanager.config.status.UserStatus;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.mapper.UserMapper;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

import java.time.Instant;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static UserEntity userEntity;
    private static UserDTO userDTO;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeAll
    static void setUp() {
        Instant date = Instant.now();

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testUser");
        userEntity.setPassword("testPassword");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCreatedAt(date);

        userDTO = new UserDTO();
        userDTO.setUsername("testUser");
        userDTO.setPassword("testPassword");
        userDTO.setStatus(UserStatus.ACTIVE);
        userDTO.setCreatedAt(date.toString());
    }

    @Test
    void getUser() {
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(userEntity));
        when(userMapper.map(any(UserEntity.class))).thenReturn(userDTO);

        StepVerifier.create(userService.getUser(123L))
                .expectNext(userDTO)
                .verifyComplete();

        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void createUser() {
        when(userMapper.map(any(UserDTO.class))).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(Mono.just(userEntity));

        StepVerifier.create(userService.createUser(userDTO))
                .expectNext(userDTO)
                .verifyComplete();

        verify(userRepository, times(1)).save(userEntity);
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Mono.just(userEntity));
        when(userRepository.save(userEntity)).thenReturn(Mono.just(userEntity));

        StepVerifier.create(userService.updateUser(123L, new UserDTO("newusername", null, null, null, null)))
                .verifyComplete();

        Assertions.assertEquals("newusername", userEntity.getUsername());

        verify(userRepository, times(1)).save(userEntity);
    }

}