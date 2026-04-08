package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Movie;
import com.myjwtai.jwtai.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(Movie movie) {
        // Ensure the ID is null to force a new entry
        movie.setId(null);
        return movieRepository.save(movie);
    }

    public List<Movie> searchMovies(String title, String genre, String language) {
        if (title != null && genre != null && language != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(title, genre, language);
        } else if (title != null && genre != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(title, genre);
        } else if (title != null && language != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndLanguageContainingIgnoreCase(title, language);
        } else if (genre != null && language != null) {
            return movieRepository.findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(genre, language);
        } else if (title != null) {
            return movieRepository.findByTitleContainingIgnoreCase(title);
        } else if (genre != null) {
            return movieRepository.findByGenreContainingIgnoreCase(genre);
        } else if (language != null) {
            return movieRepository.findByLanguageContainingIgnoreCase(language);
        } else {
            return movieRepository.findAll();
        }
    }
}
