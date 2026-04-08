package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.entity.ShowSeat;
import com.myjwtai.jwtai.payload.request.ShowRequest;
import com.myjwtai.jwtai.repository.ShowSeatRepository;
import com.myjwtai.jwtai.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shows")
public class ShowController {

    @Autowired
    private ShowService showService;
    
    @Autowired
    private ShowSeatRepository showSeatRepository;

    // Only Admins can add shows for a movie
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> addShow(@RequestBody ShowRequest showRequest) {
        Show show = showService.addShow(showRequest);
        return new ResponseEntity<>(show, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> getShowById(@PathVariable Long id) {
        Show show = showService.getShowById(id);
        return ResponseEntity.ok(show);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Show>> getAllShows() {
        List<Show> shows = showService.getAllShows();
        return ResponseEntity.ok(shows);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Show> updateShow(@PathVariable Long id, @RequestBody ShowRequest showRequest) {
        Show updatedShow = showService.updateShow(id, showRequest);
        return ResponseEntity.ok(updatedShow);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return ResponseEntity.ok().body(new HashMap<String, String>() {{
            put("message", "Show deleted successfully");
        }});
    }

    // Anyone (including Users) can view shows for a specific movie
    @GetMapping("/movie/{movieId}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Show>> getShowsForMovie(@PathVariable Long movieId) {
        List<Show> shows = showService.getShowsByMovie(movieId);
        return ResponseEntity.ok(shows);
    }
    
    // Endpoint to view the seat map for a specific show
    @GetMapping("/{showId}/seats")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShowSeat>> getSeatMapForShow(@PathVariable Long showId) {
        List<ShowSeat> seats = showSeatRepository.findByShowId(showId);
        return ResponseEntity.ok(seats);
    }
}
