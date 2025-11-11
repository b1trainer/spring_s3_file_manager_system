package com.example.filemanager.integrationTests;

import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.security.Encoder;
import com.example.filemanager.testUtils.MyContainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockUser;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

@SpringBootTest
@DirtiesContext
public class UsersControllerIntegrationTest extends MyContainersConfiguration {
    private static final String USER_NAME = "testUserController";
    private static final String USER_PASSWORD = "superStrongPassword123!";

    private WebTestClient webTestClient;
    private UserEntity user;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Encoder passwordEncoder;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(this.context)
                .apply(springSecurity())
                .configureClient()
                .build();

        userRepository.deleteAll().block();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(USER_NAME);
        userEntity.setPassword(passwordEncoder.encode(USER_PASSWORD));
        userEntity.setStatus(UserDTO.UserStatus.ACTIVE);
        userEntity.setRole(UserDTO.UserRole.ADMIN);

        user = userRepository.save(userEntity).block();
    }

    @Test
    void shouldCreateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("userName");
        userDTO.setRole(UserDTO.UserRole.USER);
        userDTO.setStatus(UserDTO.UserStatus.ACTIVE);

        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .post()
                .uri("/rest/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo(userDTO.getUsername())
                .jsonPath("$.role").isEqualTo(userDTO.getRole().getRoleName())
                .jsonPath("$.status").isEqualTo(userDTO.getStatus().getStatusValue());
    }

    @Test
    void shouldGetUser() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .get()
                .uri("/rest/v1/users/" + user.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(user.getId())
                .jsonPath("$.username").isEqualTo(user.getUsername())
                .jsonPath("$.role").isEqualTo(user.getRole().getRoleName())
                .jsonPath("$.status").isEqualTo(user.getStatus().getStatusValue());
    }

    @Test
    void shouldDeleteUser() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .delete()
                .uri("/rest/v1/users/" + user.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldUpdateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("newUserName");
        userDTO.setStatus(UserDTO.UserStatus.BLOCKED);
        userDTO.setRole(UserDTO.UserRole.MODERATOR);

        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .patch()
                .uri("/rest/v1/users/" + user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userDTO)
                .exchange()
                .expectStatus().isOk();
    }
}
