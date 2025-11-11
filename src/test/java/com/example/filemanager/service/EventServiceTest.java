package com.example.filemanager.service;

import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.entity.EventEntity;
import com.example.filemanager.mapper.EventMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.service.impl.EventServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static EventEntity eventEntity;
    private static EventDTO eventDTO;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @BeforeAll
    static void setUp() {
        eventEntity = new EventEntity();
        eventEntity.setId(1L);
        eventEntity.setStatus(EventDTO.EventStatus.CREATED);
        eventEntity.setFile(null);
        eventEntity.setTimestamp(Instant.now());
        eventEntity.setUser(null);

        eventDTO = new EventDTO();
        eventDTO.setFile(null);
        eventDTO.setStatus(EventDTO.EventStatus.CREATED);
        eventDTO.setUser(null);
        eventDTO.setTimestamp(Instant.now().toString());
    }

    @Test
    void getEvent() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.just(eventEntity));
        when(eventMapper.map(any(EventEntity.class))).thenReturn(eventDTO);

        StepVerifier.create(eventService.getEvent(123L))
                .expectNext(eventDTO)
                .expectComplete()
                .verify();

        verify(eventMapper, times(1)).map(eventEntity);
    }

    @Test
    void createEvent() {
        when(eventMapper.map(any(EventDTO.class))).thenReturn(eventEntity);
        when(eventRepository.save(eventEntity)).thenReturn(Mono.just(eventEntity));

        StepVerifier.create(eventService.createEvent(eventDTO))
                .expectNext(eventDTO)
                .expectComplete()
                .verify();

        verify(eventMapper, times(1)).map(eventDTO);
    }

    @Test
    void updateEventStatus() {
        when(eventRepository.findById(any(Long.class))).thenReturn(Mono.just(eventEntity));

        Assertions.assertEquals(EventDTO.EventStatus.CREATED, eventEntity.getStatus());

        StepVerifier.create(eventService.updateEventStatus(123L, EventDTO.EventStatus.UPDATED))
                .verifyComplete();

        Assertions.assertEquals(EventDTO.EventStatus.UPDATED, eventEntity.getStatus());
    }

}