package com.ticketing.eventservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventResponseDto {

    private Long id;
    private String title;
    private String description;
    private String venue;
    private LocalDateTime eventDate;
    private BigDecimal ticketPrice; // C
    private Integer totalSeats;

    public EventResponseDto() {}

    public EventResponseDto(Long id, String title, String description, String venue,
                            LocalDateTime eventDate, BigDecimal ticketPrice, Integer totalSeats) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.venue = venue;
        this.eventDate = eventDate;
        this.ticketPrice = ticketPrice;
        this.totalSeats = totalSeats;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }
}