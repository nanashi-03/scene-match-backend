package com.silveredsoul.scene_match.repository;

import com.silveredsoul.scene_match.model.Movie;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    boolean existsByTitle(String title);
    boolean existsByTmdbId(Long tmdbId);
    
    Optional<Movie> findByTmdbId(Long tmdbId);

    @SuppressWarnings("null")
    Optional<Movie> findById(Long id);

    List<Movie> findByTitle(String title);
    List<Movie> findByTitleContainingIgnoreCase(String title);
}