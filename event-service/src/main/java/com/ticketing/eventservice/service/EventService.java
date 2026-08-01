package com.ticketing.eventservice.service;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.entity.Event;
import com.ticketing.eventservice.exception.ResourceNotFoundException;
import com.ticketing.eventservice.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(EventRequestDto dto) {
        Event event = new Event(
                dto.getTitle(),
                dto.getDescription(),
                dto.getVenue(),
                dto.getEventDate(),
                dto.getTicketPrice(),
                dto.getTotalSeats()
        );
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }
}