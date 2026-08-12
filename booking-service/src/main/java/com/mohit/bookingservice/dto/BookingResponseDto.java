package com.mohit.bookingservice.dto;

import com.mohit.bookingservice.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookingResponseDto(
        Long id,
        Long userId,
        Long eventId,
        Integer seatsBooked,
        BigDecimal totalPrice,
        BookingStatus status,
        LocalDateTime bookingTime
) {}