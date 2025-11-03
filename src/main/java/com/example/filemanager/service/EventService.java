package com.example.filemanager.service;

import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import reactor.core.publisher.Mono;

public interface EventService {
    Mono<EventDTO> getEvent(String id);

    Mono<EventDTO> createEvent(EventDTO eventDTO);

    Mono<Void> updateEventStatus(String eventId, EventStatus status);

    Mono<Void> deleteEvent(String id);
}
