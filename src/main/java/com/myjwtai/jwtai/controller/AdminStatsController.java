package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.repository.ShowRepository;
import com.myjwtai.jwtai.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/stats")
@PreAuthorize("hasRole('ADMIN')")
public class AdminStatsController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ShowRepository showRepository;

    @GetMapping("/revenue/show/{showId}")
    public ResponseEntity<Map<String, Object>> getRevenueForShow(@PathVariable Long showId) {
        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new RuntimeException("Error: Show not found."));

        BigDecimal revenue = ticketRepository.calculateTotalRevenueForShow(showId);
        
        // Handle null if there are no confirmed tickets yet
        if (revenue == null) {
            revenue = BigDecimal.ZERO;
        }

        Map<String, Object> response = new HashMap<>();
        response.put("showId", showId);
        response.put("movieTitle", show.getMovie().getTitle());
        response.put("screenName", show.getScreen().getName());
        response.put("showTime", show.getShowTime());
        response.put("totalRevenue", revenue);

        return ResponseEntity.ok(response);
    }
}
