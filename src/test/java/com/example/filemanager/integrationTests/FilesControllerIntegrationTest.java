package com.example.filemanager.integrationTests;

import com.example.filemanager.dto.FileDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.FileEntity;
import com.example.filemanager.entity.UserEntity;
import com.example.filemanager.repository.FileRepository;
import com.example.filemanager.repository.UserRepository;
import com.example.filemanager.service.MinioS3Service;
import com.example.filemanager.testUtils.MyContainersConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@SpringBootTest
@DirtiesContext
public class FilesControllerIntegrationTest extends MyContainersConfiguration {
    private static final String USER_NAME = "testUserNameFiles";
    private static final String BUCKET_NAME = "test-bucket";
    private static final String USER_PASSWORD = "superStrongPassword123!";
    private static final String FILE_CONTENT = "Test file content";

    private WebTestClient webTestClient;
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
        userEntity.setStatus(UserDTO.UserStatus.ACTIVE);
        userEntity.setRole(UserDTO.UserRole.ADMIN);

        user = userRepository.save(userEntity).block();

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                FILE_CONTENT.getBytes()
        );

        String location = BUCKET_NAME + "/" + multipartFile.getOriginalFilename();

        fileRepository.deleteAll().block();

        FileEntity fileEntity = new FileEntity();
        fileEntity.setLocation(location);
        fileEntity.setName(multipartFile.getOriginalFilename());
        fileEntity.setStatus(FileDTO.FileStatus.ACTIVE);

        file = fileRepository.save(fileEntity).block();
        minioS3Service.putObjectToS3(location, multipartFile).block();
    }

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(context)
                .apply(springSecurity())
                .configureClient()
                .build();
    }

    @Test
    void shouldGetFile() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .get()
                .uri("/rest/v1/files/" + file.getId())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain")
                .expectBody(String.class)
                .value(content ->
                        Assertions.assertEquals(content, FILE_CONTENT)
                );
    }

    @Test
    void shouldUpdateFileStatus() {
        FileDTO.FileStatus fileStatus = FileDTO.FileStatus.ACTIVE;

        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .patch()
                .uri("/rest/v1/files/" + file.getId() + "/" + fileStatus)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldDeleteFile() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .delete()
                .uri("/rest/v1/files/" + file.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

}
