package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.ShowSeat;
import com.myjwtai.jwtai.entity.Ticket;
import com.myjwtai.jwtai.payload.request.LockSeatsRequest;
import com.myjwtai.jwtai.payload.request.TicketRequest;
import com.myjwtai.jwtai.payload.request.UnlockSeatsRequest;
import com.myjwtai.jwtai.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/lock-seats")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> lockSeats(@RequestBody LockSeatsRequest lockRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            List<ShowSeat> lockedSeats = ticketService.lockSeats(username, lockRequest);
            return ResponseEntity.ok(lockedSeats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/unlock-seats")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unlockSeats(@RequestBody UnlockSeatsRequest unlockRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            List<ShowSeat> unlockedSeats = ticketService.unlockSeats(username, unlockRequest);
            return ResponseEntity.ok(unlockedSeats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Users can book tickets
    @PostMapping("/book")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> bookTicket(@RequestBody TicketRequest ticketRequest) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Ticket ticket = ticketService.bookTicket(username, ticketRequest);
            return new ResponseEntity<>(ticket, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Users can view their own booked tickets
    @GetMapping("/my-tickets")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Ticket>> getMyTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        List<Ticket> tickets = ticketService.getUserTickets(username);
        return ResponseEntity.ok(tickets);
    }

    // Users can cancel their own tickets
    @PostMapping("/{ticketId}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelTicket(@PathVariable Long ticketId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            Ticket cancelledTicket = ticketService.cancelTicket(ticketId, username);
            return ResponseEntity.ok(cancelledTicket);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
