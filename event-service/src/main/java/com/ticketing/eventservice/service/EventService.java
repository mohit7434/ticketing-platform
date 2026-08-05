package com.ticketing.eventservice.service;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.dto.EventResponseDto;
import com.ticketing.eventservice.entity.Event;
import com.ticketing.eventservice.exception.ResourceNotFoundException;
import com.ticketing.eventservice.repository.EventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    // Paginated, Sorted, and Filtered Get All Events
    public Page<EventResponseDto> getAllEvents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String keyword
    ) {
        // 1. Determine sort direction
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // 2. Create Pageable instance
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> eventPage;

        // 3. Fetch filtered or unfiltered page
        if (keyword != null && !keyword.trim().isEmpty()) {
            eventPage = eventRepository.findByTitleContainingIgnoreCaseOrVenueContainingIgnoreCase(keyword, keyword, pageable);
        } else {
            eventPage = eventRepository.findAll(pageable);
        }

        // 4. Map Event entities to EventResponseDto
        return eventPage.map(this::mapToDto);
    }

    public Event getEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
    }
    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        existingEvent.setTitle(dto.getTitle());
        existingEvent.setDescription(dto.getDescription());
        existingEvent.setVenue(dto.getVenue());
        existingEvent.setEventDate(dto.getEventDate());
        existingEvent.setTicketPrice(dto.getTicketPrice());
        existingEvent.setTotalSeats(dto.getTotalSeats());

        Event updatedEvent = eventRepository.save(existingEvent);
        return mapToDto(updatedEvent);
    }

    // Delete an event by ID
    public void deleteEvent(Long id) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
        eventRepository.delete(existingEvent);
    }

    // Private Helper Method to Map Entity -> DTO
    private EventResponseDto mapToDto(Event event) {
        EventResponseDto dto = new EventResponseDto();
        dto.setId(event.getId());
        dto.setTitle(event.getTitle());
        dto.setDescription(event.getDescription());
        dto.setVenue(event.getVenue());
        dto.setEventDate(event.getEventDate());
        dto.setTicketPrice(event.getTicketPrice());
        dto.setTotalSeats(event.getTotalSeats());
        return dto;
    }
    // Decrement seats when a booking is confirmed
    public EventResponseDto reserveSeats(Long id, int seatsToBook) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        if (event.getTotalSeats() < seatsToBook) {
            throw new IllegalStateException("Not enough seats available. Requested: "
                    + seatsToBook + ", Available: " + event.getTotalSeats());
        }

        event.setTotalSeats(event.getTotalSeats() - seatsToBook);
        Event updatedEvent = eventRepository.save(event);
        return mapToDto(updatedEvent);
    }
}