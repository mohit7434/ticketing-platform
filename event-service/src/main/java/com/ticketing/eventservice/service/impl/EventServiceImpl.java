package com.ticketing.eventservice.service.impl;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.dto.EventResponseDto;
import com.ticketing.eventservice.entity.Event;
import com.ticketing.eventservice.exception.ResourceNotFoundException;
import com.ticketing.eventservice.repository.EventRepository;
import com.ticketing.eventservice.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public EventResponseDto createEvent(EventRequestDto dto) {
        Event event = new Event(
                dto.getTitle(),
                dto.getDescription(),
                dto.getVenue(),
                dto.getEventDate(),
                dto.getTicketPrice(),
                dto.getTotalSeats()
        );
        Event saved = eventRepository.save(event);
        return mapToDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDto> getAllEvents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String keyword
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Event> eventPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            eventPage = eventRepository.findByIsActiveTrueAndTitleContainingIgnoreCaseOrIsActiveTrueAndVenueContainingIgnoreCase(
                    keyword, keyword, pageable);
        } else {
            eventPage = eventRepository.findByIsActiveTrue(pageable);
        }

        return eventPage.map(this::mapToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));
        return mapToDto(event);
    }

    @Override
    @Transactional
    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        Event existingEvent = eventRepository.findByIdAndIsActiveTrue(id)
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

    @Override
    @Transactional
    public EventResponseDto reserveSeats(Long id, int seatsToBook) {
        Event event = eventRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        if (event.getTotalSeats() < seatsToBook) {
            throw new IllegalStateException("Not enough seats available. Requested: "
                    + seatsToBook + ", Available: " + event.getTotalSeats());
        }

        event.setTotalSeats(event.getTotalSeats() - seatsToBook);
        Event updatedEvent = eventRepository.save(event);
        return mapToDto(updatedEvent);
    }

    // Soft delete: sets isActive to false without purging the record
    @Override
    @Transactional
    public void deleteEvent(Long id) {
        Event existingEvent = eventRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with ID: " + id));

        existingEvent.setIsActive(false);
        eventRepository.save(existingEvent);
    }

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
}