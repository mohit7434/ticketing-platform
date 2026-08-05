package com.ticketing.eventservice.controller;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.dto.EventResponseDto;
import com.ticketing.eventservice.service.EventService;
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
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto dto) {
        return new ResponseEntity<>(eventService.createEvent(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<EventResponseDto>> getAllEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "eventDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(eventService.getAllEvents(page, size, sortBy, sortDir, keyword));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long id, @RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.updateEvent(id, dto));
    }

    @PatchMapping("/{id}/reserve")
    public ResponseEntity<EventResponseDto> reserveSeats(
            @PathVariable Long id,
            @RequestParam int seats
    ) {
        return ResponseEntity.ok(eventService.reserveSeats(id, seats));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}