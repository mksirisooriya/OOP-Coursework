package com.ticketing.system.repository;

import com.ticketing.system.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByAvailable(boolean available);

    long countByAvailable(boolean available);

    @Query(value = "SELECT * FROM tickets WHERE available = true ORDER BY created_at ASC LIMIT 1", nativeQuery = true)
    List<Ticket> findFirstAvailableTicket();

    @Query("SELECT COUNT(t) FROM Ticket t")
    long getTotalTicketCount();

    Optional<Ticket> findTopByOrderByTicketNumberDesc();
}