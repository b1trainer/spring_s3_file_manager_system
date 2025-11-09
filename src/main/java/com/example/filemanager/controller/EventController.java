package com.example.filemanager.controller;

import com.example.filemanager.config.status.EventStatus;
import com.example.filemanager.dto.EventDTO;
import com.example.filemanager.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.naming.ServiceUnavailableException;

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
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка создания события: " + e))
                .map(event -> ResponseEntity.status(HttpStatus.CREATED).body(event));
    }

    @GetMapping("/{eventId}")
    public Mono<ResponseEntity<EventDTO>> getEvent(@PathVariable Long eventId) {
        return eventService.getEvent(eventId)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка получения события: " + e))
                .map((event) -> ResponseEntity.ok().body(event))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{eventId}/{status}")
    public Mono<ResponseEntity<EventDTO>> updateEvent(@PathVariable Long eventId, @PathVariable EventStatus status) {
        return eventService.updateEventStatus(eventId, status)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка обновления статуса события: " + e))
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    @DeleteMapping("/{eventId}")
    public Mono<ResponseEntity<Void>> deleteEvent(@PathVariable Long eventId) {
        return eventService.deleteEvent(eventId)
                .onErrorMap(RuntimeException.class, e -> new ServiceUnavailableException("Ошибка удаления события: " + e))
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
