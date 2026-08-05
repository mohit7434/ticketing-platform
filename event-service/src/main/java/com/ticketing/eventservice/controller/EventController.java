package com.ticketing.eventservice.controller;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.dto.EventResponseDto;
import com.ticketing.eventservice.entity.Event;
import com.ticketing.eventservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody EventRequestDto request) {
        Event createdEvent = eventService.createEvent(request);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }
    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword
    ) {
        Page<EventResponseDto> events = eventService.getAllEvents(page, size, sortBy, sortDir, keyword);
        return ResponseEntity.ok(events);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(
            @PathVariable Long id,
            @RequestBody EventRequestDto dto
    ) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully with ID: " + id);
    }
    @PatchMapping("/{id}/reserve")
    public ResponseEntity<EventResponseDto> reserveSeats(
            @PathVariable Long id,
            @RequestParam int seats
    ) {
        return ResponseEntity.ok(eventService.reserveSeats(id, seats));
    }
}