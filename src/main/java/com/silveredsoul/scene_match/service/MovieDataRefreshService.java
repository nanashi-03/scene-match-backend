package com.silveredsoul.scene_match.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieDataRefreshService {

    private final TmdbService tmdbService;
    private final MovieRepository movieRepository;

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    public void refreshMovieData() {
        movieRepository.deleteAll();

        List<Movie> movies = tmdbService.getCombinedUniqueMovies();

        System.out.println("Refreshing movie data... Found " + movies.size() + " movies to process.");

        try {
            for (Movie movieData : movies) {
                if (movieRepository.existsByTmdbId(movieData.getTmdbId())) {
                    continue;
                }

                movieRepository.save(movieData);
            }
            System.out.println("Movie data refreshed successfully.");
        } catch (Exception e) {
            System.err.println("Error during movie data refresh: " + e.getMessage());
        }

    }
}