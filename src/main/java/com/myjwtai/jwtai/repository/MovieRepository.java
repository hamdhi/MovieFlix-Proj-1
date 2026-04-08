package com.myjwtai.jwtai.repository;

import com.myjwtai.jwtai.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByLanguageContainingIgnoreCase(String language);
    List<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(String title, String genre);
    List<Movie> findByTitleContainingIgnoreCaseAndLanguageContainingIgnoreCase(String title, String language);
    List<Movie> findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(String genre, String language);
    List<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(String title, String genre, String language);
}
