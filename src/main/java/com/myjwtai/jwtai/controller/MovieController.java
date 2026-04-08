package com.myjwtai.jwtai.controller;

import com.myjwtai.jwtai.entity.Movie;
import com.myjwtai.jwtai.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies") // Changed base path to /api/v1/movies
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/add") // Changed endpoint to /add
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.addMovie(movie);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language) {
        List<Movie> movies = movieService.searchMovies(title, genre, language);
        return ResponseEntity.ok(movies);
    }
}
