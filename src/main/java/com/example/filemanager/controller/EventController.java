package com.example.filemanager.controller;

import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rest/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Mono<ResponseEntity<EventDTO>> createEvent(@RequestBody EventDTO eventDTO) {
        return eventService.createEvent(eventDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{eventId}")
    public Mono<ResponseEntity<EventDTO>> getEvent(@PathVariable Long eventId) {
        return eventService.getEvent(eventId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{eventId}/{status}")
    public Mono<ResponseEntity<EventDTO>> updateEvent(@PathVariable Long eventId, @PathVariable EventStatus status) {
        return eventService.updateEventStatus(eventId, status)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{eventId}")
    public Mono<ResponseEntity<Void>> deleteEvent(@PathVariable Long eventId) {
        return eventService.deleteEvent(eventId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
