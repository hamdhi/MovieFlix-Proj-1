package com.myjwtai.jwtai.repository;

import com.myjwtai.jwtai.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUserId(Long userId);
    
    // Query to calculate total revenue from confirmed tickets for a specific show
    @Query("SELECT SUM(t.totalAmount) FROM Ticket t WHERE t.show.id = :showId AND t.status = 'CONFIRMED'")
    BigDecimal calculateTotalRevenueForShow(Long showId);
}
