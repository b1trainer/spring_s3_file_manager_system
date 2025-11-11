package com.example.filemanager.integrationTests;

import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.dto.UserDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.testUtils.MyContainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*;

@SpringBootTest
@DirtiesContext
public class EventsControllerIntegrationTest extends MyContainersConfiguration {
    private WebTestClient webTestClient;
    private static EventEntity createdEvent;

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
        eventEntity.setUser(null);
        eventEntity.setFile(null);
        eventEntity.setStatus(EventDTO.EventStatus.DELETED);

        createdEvent = eventRepository.save(eventEntity).block();
    }

    @Test
    void shouldCreateEvent() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setFile(null);
        eventDTO.setUser(null);
        eventDTO.setStatus(EventDTO.EventStatus.CREATED);

        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .post()
                .uri("/rest/v1/events")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(eventDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.user").isEqualTo(eventDTO.getUser())
                .jsonPath("$.file").isEqualTo(eventDTO.getFile())
                .jsonPath("$.status").isEqualTo(eventDTO.getStatus().getValue());
    }

    @Test
    void shouldGetEvent() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .get()
                .uri("/rest/v1/events/" + createdEvent.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(createdEvent.getId())
                .jsonPath("$.user").isEqualTo(createdEvent.getUser())
                .jsonPath("$.file").isEqualTo(createdEvent.getFile())
                .jsonPath("$.status").isEqualTo(createdEvent.getStatus().getValue());
    }

    @Test
    void shouldDeleteEvent() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .delete()
                .uri("/rest/v1/events/" + createdEvent.getId())
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    void shouldUpdateEventStatus() {
        webTestClient
                .mutateWith(mockUser().roles(UserDTO.UserRole.ADMIN.getRoleName()))
                .patch()
                .uri("/rest/v1/events/" + createdEvent.getId() + "/" + EventDTO.EventStatus.UPDATED)
                .exchange()
                .expectStatus().isOk();
    }
}
