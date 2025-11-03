package com.example.filemanager.service.impl;

import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.mapper.EventMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.service.EventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    public Mono<EventDTO> getEvent(String eventId) {
        Long id = Long.parseLong(eventId);

        return eventRepository.findById(id)
                .flatMap(eventEntity -> Mono.just(eventMapper.map(eventEntity)));
    }

    @Override
    public Mono<EventDTO> createEvent(EventDTO eventDTO) {
        return eventRepository.save(eventMapper.map(eventDTO)).thenReturn(eventDTO);
    }

    @Override
    public Mono<Void> updateEventStatus(String eventId, EventStatus status) {
        Long id = Long.parseLong(eventId);

        return eventRepository.findById(id)
                .flatMap(eventEntity -> {
                    eventEntity.setStatus(status);
                    return Mono.empty();
                });
    }

    @Override
    public Mono<Void> deleteEvent(String eventId) {
        Long id = Long.parseLong(eventId);

        return eventRepository.deleteById(id);
    }
}
