package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Seat;
import com.myjwtai.jwtai.repository.SeatRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {

    @Autowired
    private SeatRepository seatRepository;

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Seat> addSeat(@Valid @RequestBody Seat seat) {
        seat.setId(null); // Ensure new seat
        Seat savedSeat = seatRepository.save(seat);
        return new ResponseEntity<>(savedSeat, HttpStatus.CREATED);
    }
}
