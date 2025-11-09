package com.example.filemanager.integrationTests;

import com.example.filemanager.config.UserRole;
import com.example.filemanager.config.status.FileStatus;
import com.example.filemanager.config.status.UserStatus;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.security.CustomPrincipal;
import com.example.filemanager.service.MinioS3Service;
import com.example.filemanager.testUtils.MyContainersConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Collections;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@SpringBootTest
public class FilesControllerIntegrationTest extends MyContainersConfiguration {
    private static final String USER_NAME = "testUserNameFiles";
    private static final String BUCKET_NAME = "test-bucket";
    private static final String USER_PASSWORD = "superStrongPassword123!";

    private WebTestClient webTestClient;
    private UsernamePasswordAuthenticationToken auth;
    private static UserEntity user;
    private static FileEntity file;

    @Autowired
    private ApplicationContext context;

    @BeforeAll
    static void beforeAll(@Autowired MinioS3Service minioS3Service, @Autowired UserRepository userRepository, @Autowired FileRepository fileRepository) {
        userRepository.deleteAll().block();

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(USER_NAME);
        userEntity.setPassword(USER_PASSWORD);
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setRole(UserRole.ADMIN);

        user = userRepository.save(userEntity).block();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Test file content".getBytes()
        );

        String location = BUCKET_NAME + "/" + multipartFile.getOriginalFilename();

        fileRepository.deleteAll().block();

        FileEntity fileEntity = new FileEntity();
        fileEntity.setLocation(location);
        fileEntity.setUserId(user.getId());
        fileEntity.setName(multipartFile.getOriginalFilename());
        fileEntity.setStatus(FileStatus.ACTIVE);

        file = fileRepository.save(fileEntity).block();
        minioS3Service.putObjectToS3(location, multipartFile).block();
    }

    @BeforeEach
    void setUp() {
        CustomPrincipal principal = new CustomPrincipal();
        principal.setId(user.getId());
        principal.setName(user.getUsername());
        principal.setStatus(UserStatus.ACTIVE.getStatusValue());

        auth = new UsernamePasswordAuthenticationToken(
                principal, null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + UserRole.ADMIN.getRoleName()))
        );

        webTestClient = WebTestClient
                .bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    void shouldGetFile() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .get()
                .uri("/rest/v1/files/" + file.getId())
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldUpdateFileStatus() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .patch()
                .uri("/rest/v1/files/" + file.getId() + "/" + FileStatus.ARCHIVE)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldReturnListOfAvailableFiles() {
        webTestClient
                .mutateWith(mockAuthentication(auth))
                .get()
                .uri("/rest/v1/files")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldDeleteFile() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .delete()
                .uri("/rest/v1/files/" + file.getId())
                .exchange()
                .expectStatus().isNoContent();
    }


    //    @Test
    //    void shouldLoadFile() {
    //    }
}
