package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Movie;
import com.myjwtai.jwtai.entity.Screen;
import com.myjwtai.jwtai.entity.Seat;
import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.entity.ShowSeat;
import com.myjwtai.jwtai.payload.request.ShowRequest;
import com.myjwtai.jwtai.repository.MovieRepository;
import com.myjwtai.jwtai.repository.ScreenRepository;
import com.myjwtai.jwtai.repository.SeatRepository;
import com.myjwtai.jwtai.repository.ShowRepository;
import com.myjwtai.jwtai.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ScreenRepository screenRepository;
    
    @Autowired
    private SeatRepository seatRepository;
    
    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Transactional
    public Show addShow(ShowRequest showRequest) {
        Movie movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Error: Movie not found."));

        Screen screen = screenRepository.findById(showRequest.getScreenId())
                .orElseThrow(() -> new RuntimeException("Error: Screen not found."));

        Show show = new Show();
        show.setMovie(movie);
        show.setScreen(screen);
        show.setShowTime(showRequest.getShowTime());
        show.setPrice(showRequest.getPrice()); // Set price

        Show savedShow = showRepository.save(show);
        
        // Generate ShowSeats based on the Screen's physical seats
        List<Seat> seats = seatRepository.findByScreenId(screen.getId());
        for (Seat seat : seats) {
            ShowSeat showSeat = new ShowSeat();
            showSeat.setShow(savedShow);
            showSeat.setSeat(seat);
            showSeat.setStatus(ShowSeat.SeatStatus.AVAILABLE);
            showSeatRepository.save(showSeat);
        }

        return savedShow;
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    public Show getShowById(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Show not found."));
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    @Transactional
    public Show updateShow(Long id, ShowRequest showRequest) {
        Show existingShow = showRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Show not found."));

        if (showRequest.getMovieId() != null) {
            Movie movie = movieRepository.findById(showRequest.getMovieId())
                    .orElseThrow(() -> new RuntimeException("Error: Movie not found."));
            existingShow.setMovie(movie);
        }

        if (showRequest.getScreenId() != null) {
            // Note: Changing the screen for an existing show can be very complex because
            // existing ShowSeats are tied to the old screen's physical seats.
            // In a real system, you might prevent this or require a complete re-generation of ShowSeats.
            // For simplicity here, we only update the reference, but this could cause data inconsistencies if not handled carefully.
            Screen screen = screenRepository.findById(showRequest.getScreenId())
                    .orElseThrow(() -> new RuntimeException("Error: Screen not found."));
            existingShow.setScreen(screen);
        }

        if (showRequest.getShowTime() != null) {
            existingShow.setShowTime(showRequest.getShowTime());
        }
        
        if (showRequest.getPrice() != null) {
            existingShow.setPrice(showRequest.getPrice());
        }

        return showRepository.save(existingShow);
    }

    @Transactional
    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new RuntimeException("Error: Show not found.");
        }
        // Note: Deleting a show requires deleting associated ShowSeats and Tickets first (or using cascading).
        showRepository.deleteById(id);
    }
}
