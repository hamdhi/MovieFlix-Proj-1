package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Movie;
import com.myjwtai.jwtai.entity.Show;
import com.myjwtai.jwtai.payload.request.ShowRequest;
import com.myjwtai.jwtai.repository.MovieRepository;
import com.myjwtai.jwtai.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private MovieRepository movieRepository;

    public Show addShow(ShowRequest showRequest) {
        Movie movie = movieRepository.findById(showRequest.getMovieId())
                .orElseThrow(() -> new RuntimeException("Error: Movie not found."));

        Show show = new Show();
        show.setMovie(movie);
        show.setShowTime(showRequest.getShowTime());
        show.setAvailableSeats(showRequest.getAvailableSeats());

        return showRepository.save(show);
    }

    public List<Show> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }
}
