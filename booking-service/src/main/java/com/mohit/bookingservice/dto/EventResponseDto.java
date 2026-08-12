package com.mohit.bookingservice.dto;
import java.math.BigDecimal;

public record EventResponseDto(
        Long id,
        String title,
        String description,
        String venue,
        String eventDate,
        BigDecimal ticketPrice,
        Integer totalSeats
) {}