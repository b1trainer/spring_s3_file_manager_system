package com.example.filemanager.service.impl;

import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.service.EventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventServiceImpl implements EventService {
    @Override
    public Mono<EventDTO> getEvent(String id) {
        return null;
    }

    @Override
    public Mono<EventDTO> createEvent(EventDTO eventDTO) {
        return null;
    }

    @Override
    public Mono<EventDTO> updateEvent(String eventId, EventDTO eventDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteEvent(String id) {
        return null;
    }
}
