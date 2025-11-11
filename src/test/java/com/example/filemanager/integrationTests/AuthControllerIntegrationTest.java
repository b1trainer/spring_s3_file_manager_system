package com.example.filemanager.integrationTests;

import com.example.filemanager.dto.AuthRequestDTO;
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

@SpringBootTest
@DirtiesContext
public class AuthControllerIntegrationTest extends MyContainersConfiguration {
    private static final String USER_NAME = "testUserAuth";
    private static final String USER_PASSWORD = "superStrongPassword123!";

    private UserEntity user;
    private WebTestClient webTestClient;

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
    void shouldLogInForExistUser() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO(user.getUsername(), USER_PASSWORD);

        webTestClient
                .post()
                .uri("/rest/v1/auth/logIn")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.userid").isNotEmpty()
                .jsonPath("$.issued_at").isNotEmpty()
                .jsonPath("$.expire_at").isNotEmpty()
                .jsonPath("$.token").isNotEmpty();
    }

    @Test
    void shouldSignInForNewUser() {
        AuthRequestDTO authRequestDTO = new AuthRequestDTO("notExistUser", USER_PASSWORD);

        webTestClient
                .post()
                .uri("/rest/v1/auth/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(authRequestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.username").isEqualTo(authRequestDTO.getUsername())
                .jsonPath("$.status").isEqualTo(UserDTO.UserStatus.ACTIVE.getStatusValue())
                .jsonPath("$.role").isEqualTo(UserDTO.UserRole.USER.getRoleName())
        ;
    }
}
