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
}
