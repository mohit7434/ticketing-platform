package com.mohit.bookingservice.client;

import com.mohit.bookingservice.dto.EventResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "event-service", url = "http://localhost:8082/api/v1/events")
public interface EventClient {

    @GetMapping("/{id}")
    EventResponseDto getEventById(@PathVariable("id") Long id);

    @PatchMapping("/{id}/reserve")
    EventResponseDto reserveSeats(
            @PathVariable("id") Long id,
            @RequestParam("seats") int seats
    );
}