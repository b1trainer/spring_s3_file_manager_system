package com.example.filemanager.service.impl;

import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.mapper.EventMapper;
import com.example.filemanager.repository.EventRepository;
import com.example.filemanager.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Mono<EventDTO> getEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .flatMap(eventEntity -> Mono.just(eventMapper.map(eventEntity)));
    }

    @Override
    @Transactional
    public Mono<EventDTO> createEvent(EventDTO eventDTO) {
        return eventRepository.save(eventMapper.map(eventDTO)).thenReturn(eventDTO);
    }

    @Override
    @Transactional
    public Mono<Void> updateEventStatus(Long eventId, EventStatus status) {
        return eventRepository.findById(eventId)
                .flatMap(eventEntity -> {
                    eventEntity.setStatus(status);
                    return Mono.empty();
                });
    }

    @Override
    @Transactional
    public Mono<Void> deleteEvent(Long eventId) {
        return eventRepository.deleteById(eventId);
    }
}
