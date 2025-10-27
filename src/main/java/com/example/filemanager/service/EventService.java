package com.example.filemanager.service;

import com.example.filemanager.dto.EventDTO;
import reactor.core.publisher.Mono;

public interface EventService {
    public Mono<EventDTO> getEvent(String id);

    public Mono<EventDTO> createEvent(EventDTO eventDTO);

    public Mono<EventDTO> updateEvent(String eventId, EventDTO eventDTO);

    public Mono<Void> deleteEvent(String id);
}
