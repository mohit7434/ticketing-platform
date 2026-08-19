package com.mohit.bookingservice.controller;

import com.mohit.bookingservice.dto.BookingRequestDto;
import com.mohit.bookingservice.dto.BookingResponseDto;
import com.mohit.bookingservice.security.JwtTokenProvider;
import com.mohit.bookingservice.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final JwtTokenProvider tokenProvider;

    public BookingController(BookingService bookingService, JwtTokenProvider tokenProvider) {
        this.bookingService = bookingService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(
            @RequestBody BookingRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long userId = extractUserIdFromRequest(request);
        BookingResponseDto response = bookingService.createBooking(requestDto, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);
        return ResponseEntity.ok(bookingService.getBookingsByUser(userId));
    }

    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return tokenProvider.getUserId(token);
        }
        throw new IllegalStateException("Missing or invalid Authorization header");
    }
}