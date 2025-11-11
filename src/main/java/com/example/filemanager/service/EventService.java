package com.example.filemanager.service;

import com.example.filemanager.dto.EventDTO;
import reactor.core.publisher.Mono;

public interface EventService {
    Mono<EventDTO> getEvent(Long eventId);

    Mono<EventDTO> createEvent(EventDTO eventDTO);

    Mono<Void> updateEventStatus(Long eventId, EventDTO.EventStatus status);

    Mono<Void> deleteEvent(Long eventId);
}
