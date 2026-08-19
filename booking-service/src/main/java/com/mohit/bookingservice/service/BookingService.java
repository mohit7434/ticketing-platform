package com.mohit.bookingservice.service;

import com.mohit.bookingservice.client.EventClient;
import com.mohit.bookingservice.dto.BookingRequestDto;
import com.mohit.bookingservice.dto.BookingResponseDto;
import com.mohit.bookingservice.dto.EventResponseDto;
import com.mohit.bookingservice.entity.Booking;
import com.mohit.bookingservice.entity.BookingStatus;
import com.mohit.bookingservice.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventClient eventClient;

    public BookingService(BookingRepository bookingRepository, EventClient eventClient) {
        this.bookingRepository = bookingRepository;
        this.eventClient = eventClient;
    }

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request, Long userId) {
        // 1. Inter-service call: Reserve seats in event-service via Feign Client
        EventResponseDto event = eventClient.reserveSeats(request.eventId(), request.seatsToBook());

        // 2. Calculate total price
        BigDecimal totalPrice = event.ticketPrice().multiply(BigDecimal.valueOf(request.seatsToBook()));

        // 3. Save booking record
        Booking booking = new Booking(
                userId,
                request.eventId(),
                request.seatsToBook(),
                totalPrice,
                BookingStatus.CONFIRMED,
                LocalDateTime.now()
        );

        Booking savedBooking = bookingRepository.save(booking);
        return mapToDto(savedBooking);
    }

    public List<BookingResponseDto> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private BookingResponseDto mapToDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getUserId(),
                booking.getEventId(),
                booking.getSeatsBooked(),
                booking.getTotalPrice(),
                booking.getStatus(),
                booking.getBookingTime()
        );
    }
}