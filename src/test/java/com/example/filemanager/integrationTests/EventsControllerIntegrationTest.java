package com.example.filemanager.integrationTests;

import com.example.filemanager.config.UserRole;
import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.testUtils.MyContainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@SpringBootTest
public class EventsControllerIntegrationTest extends MyContainersConfiguration {
    private WebTestClient webTestClient;
    private static Long createdEventId;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private EventRepository eventRepository;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(this.context)
                .apply(springSecurity())
                .configureClient()
                .build();

        eventRepository.deleteAll().block();

        EventEntity eventEntity = new EventEntity();
        eventEntity.setUserId(null);
        eventEntity.setFileId(null);
        eventEntity.setStatus(EventStatus.DELETED);

        createdEventId = eventRepository.save(eventEntity).map(EventEntity::getId).block();
    }

    @Test
    void shouldCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setFileId(null);
        eventDTO.setUserId(null);
        eventDTO.setStatus(EventStatus.CREATED);

        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .post()
                .uri("/rest/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void shouldGetEvent() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .get()
                .uri("/rest/v1/events/" + createdEventId)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldDeleteEvent() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .delete()
                .uri("/rest/v1/events/" + createdEventId)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldUpdateEventStatus() {
        webTestClient
                .mutateWith(mockUser().roles(UserRole.ADMIN.getRoleName()))
                .patch()
                .uri("/rest/v1/events/" + createdEventId + "/" + EventStatus.UPDATED)
                .exchange()
                .expectStatus().isOk();
    }
}
