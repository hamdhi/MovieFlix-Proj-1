package com.myjwtai.jwtai.service;

import com.myjwtai.jwtai.entity.Movie;
import com.myjwtai.jwtai.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Movie not found."));
    }

    @Transactional
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error: Movie not found."));

        // Update fields
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setDurationMinutes(updatedMovie.getDurationMinutes());
        existingMovie.setLanguage(updatedMovie.getLanguage());
        existingMovie.setReleaseDate(updatedMovie.getReleaseDate());
        existingMovie.setGenre(updatedMovie.getGenre());

        return movieRepository.save(existingMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Error: Movie not found.");
        }
        // Note: In a real app, you might want to perform soft deletes or handle cascading
        // deletes for Shows and Tickets linked to this movie. For now, this assumes no constraints prevent deletion.
        movieRepository.deleteById(id);
    }

    public Page<Movie> searchMovies(String title, String genre, String language, Pageable pageable) {
        if (title != null && genre != null && language != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(title, genre, language, pageable);
        } else if (title != null && genre != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(title, genre, pageable);
        } else if (title != null && language != null) {
            return movieRepository.findByTitleContainingIgnoreCaseAndLanguageContainingIgnoreCase(title, language, pageable);
        } else if (genre != null && language != null) {
            return movieRepository.findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(genre, language, pageable);
        } else if (title != null) {
            return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
        } else if (genre != null) {
            return movieRepository.findByGenreContainingIgnoreCase(genre, pageable);
        } else if (language != null) {
            return movieRepository.findByLanguageContainingIgnoreCase(language, pageable);
        } else {
            return movieRepository.findAll(pageable);
        }
    }
}
