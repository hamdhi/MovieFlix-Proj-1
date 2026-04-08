package com.myjwtai.jwtai.repository;

import com.myjwtai.jwtai.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Movie> findByGenreContainingIgnoreCase(String genre, Pageable pageable);
    Page<Movie> findByLanguageContainingIgnoreCase(String language, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(String title, String genre, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCaseAndLanguageContainingIgnoreCase(String title, String language, Pageable pageable);
    Page<Movie> findByGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(String genre, String language, Pageable pageable);
    Page<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCaseAndLanguageContainingIgnoreCase(String title, String genre, String language, Pageable pageable);
}
