package com.example.filemanager.testUtils;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class MyContainersConfiguration {

    @Container
    @ServiceConnection
    protected static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0")
            .withDatabaseName("file_manager")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);;

    @Container
    protected static final GenericContainer<?> minioContainer = new GenericContainer<>("minio/minio")
            .withExposedPorts(9000)
            .withEnv("MINIO_ROOT_USER", "minioadmin")
            .withEnv("MINIO_ROOT_PASSWORD", "minioadmin")
            .withCommand("server", "/data")
            .withReuse(true);;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("minio.endpoint", () ->
                "http://" + minioContainer.getHost() + ":" + minioContainer.getFirstMappedPort());
        registry.add("minio.access-key", () -> "minioadmin");
        registry.add("minio.secret-key", () -> "minioadmin");
        registry.add("minio.region", () -> "us-east-1");
    }
}
