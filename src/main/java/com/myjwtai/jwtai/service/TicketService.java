package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.entity.Ticket;
import com.myjwtai.jwtai.entity.User;
import com.myjwtai.jwtai.payload.request.TicketRequest;
import com.myjwtai.jwtai.repository.ShowRepository;
import com.myjwtai.jwtai.repository.TicketRepository;
import com.myjwtai.jwtai.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Ticket bookTicket(String username, TicketRequest ticketRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        Show show = showRepository.findById(ticketRequest.getShowId())
                .orElseThrow(() -> new RuntimeException("Error: Show not found."));

        if (show.getAvailableSeats() < ticketRequest.getNumberOfSeats()) {
            throw new RuntimeException("Error: Not enough seats available.");
        }

        // Deduct seats
        show.setAvailableSeats(show.getAvailableSeats() - ticketRequest.getNumberOfSeats());
        showRepository.save(show);

        // Create Ticket
        Ticket ticket = new Ticket();
        ticket.setUser(user);
        ticket.setShow(show);
        ticket.setNumberOfSeats(ticketRequest.getNumberOfSeats());
        ticket.setBookingTime(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public List<Ticket> getUserTickets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));
        return ticketRepository.findByUserId(user.getId());
    }
}
