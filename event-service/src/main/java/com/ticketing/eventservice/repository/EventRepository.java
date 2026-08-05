package com.ticketing.eventservice.repository;

import com.ticketing.eventservice.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByIsActiveTrue(Pageable pageable);

    Page<Event> findByIsActiveTrueAndTitleContainingIgnoreCaseOrIsActiveTrueAndVenueContainingIgnoreCase(
            String titleKeyword,
            String venueKeyword,
            Pageable pageable
    );

    Optional<Event> findByIdAndIsActiveTrue(Long id);
}