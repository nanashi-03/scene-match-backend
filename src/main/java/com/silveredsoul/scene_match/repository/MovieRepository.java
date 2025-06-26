package com.silveredsoul.scene_match.repository;

import com.silveredsoul.scene_match.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);
    boolean existsByTmdbId(Long tmdbId);
    Movie findByTmdbId(Long tmdbId);
}