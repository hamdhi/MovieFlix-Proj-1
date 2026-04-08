package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.entity.ShowSeat;
import com.myjwtai.jwtai.entity.Ticket;
import com.myjwtai.jwtai.entity.User;
import com.myjwtai.jwtai.payload.request.TicketRequest;
import com.myjwtai.jwtai.repository.ShowRepository;
import com.myjwtai.jwtai.repository.ShowSeatRepository;
import com.myjwtai.jwtai.repository.TicketRepository;
import com.myjwtai.jwtai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Transactional
    public Ticket bookTicket(String username, TicketRequest ticketRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Show show = showRepository.findById(ticketRequest.getShowId())
                .orElseThrow(() -> new RuntimeException("Error: Show not found."));

        List<ShowSeat> selectedSeats = showSeatRepository.findAllById(ticketRequest.getShowSeatIds());
        
        if (selectedSeats.size() != ticketRequest.getShowSeatIds().size()) {
             throw new RuntimeException("Error: One or more selected seats do not exist.");
        }

        // Check if all selected seats are available
        for (ShowSeat seat : selectedSeats) {
            if (!seat.getStatus().equals(ShowSeat.SeatStatus.AVAILABLE)) {
                throw new RuntimeException("Error: Seat " + seat.getSeat().getSeatNumber() + " is already booked.");
            }
            if (!seat.getShow().getId().equals(show.getId())) {
                 throw new RuntimeException("Error: Seat " + seat.getSeat().getSeatNumber() + " does not belong to this show.");
            }
        }

        // Calculate total amount
        BigDecimal totalAmount = show.getPrice().multiply(BigDecimal.valueOf(selectedSeats.size()));

        // Create Ticket
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setBookingTime(LocalDateTime.now());
        ticket.setTotalAmount(totalAmount);
        ticket.setStatus(Ticket.TicketStatus.CONFIRMED);
        
        Ticket savedTicket = ticketRepository.save(ticket);

        // Mark seats as booked and associate with the ticket
        for (ShowSeat seat : selectedSeats) {
            seat.setStatus(ShowSeat.SeatStatus.BOOKED);
            seat.setTicket(savedTicket);
            showSeatRepository.save(seat);
        }

        return savedTicket;
    }

    public List<Ticket> getUserTickets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        return ticketRepository.findByUserId(user.getId());
    }

    @Transactional
    public Ticket cancelTicket(Long ticketId, String username) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Error: Ticket not found."));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Verify ownership
        if (!ticket.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Error: You are not authorized to cancel this ticket.");
        }

        // Check if already cancelled
        if (ticket.getStatus().equals(Ticket.TicketStatus.CANCELLED)) {
            throw new RuntimeException("Error: This ticket has already been cancelled.");
        }

        // Find and release the associated seats
        List<ShowSeat> bookedSeats = showSeatRepository.findByTicketId(ticket.getId());
        for (ShowSeat seat : bookedSeats) {
            seat.setStatus(ShowSeat.SeatStatus.AVAILABLE);
            seat.setTicket(null); // Remove association
            showSeatRepository.save(seat);
        }

        // Update ticket status
        ticket.setStatus(Ticket.TicketStatus.CANCELLED);
        return ticketRepository.save(ticket);
    }
}
