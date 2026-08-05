package com.ticketing.eventservice.service;

import com.ticketing.eventservice.dto.EventRequestDto;
import com.ticketing.eventservice.dto.EventResponseDto;
import org.springframework.data.domain.Page;

public interface EventService {

    EventResponseDto createEvent(EventRequestDto dto);

    Page<EventResponseDto> getAllEvents(
            int page,
            int size,
            String sortBy,
            String sortDir,
            String keyword
    );

    EventResponseDto getEventById(Long id);

    EventResponseDto updateEvent(Long id, EventRequestDto dto);

    EventResponseDto reserveSeats(Long id, int seatsToBook);

    void deleteEvent(Long id);
}