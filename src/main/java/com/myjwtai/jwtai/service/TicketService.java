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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        // Create Ticket
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setBookingTime(LocalDateTime.now());
        
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
}
