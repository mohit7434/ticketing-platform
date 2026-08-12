package com.mohit.bookingservice.dto;

public record BookingRequestDto(
        Long eventId,
        Integer seatsToBook
) {}
