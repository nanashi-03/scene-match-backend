package com.silveredsoul.scene_match.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.repository.MovieRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MovieDataRefreshService {

    private final TmdbService tmdbService;
    private final MovieRepository movieRepository; // Assuming you have this

    @Scheduled(fixedRate = 7 * 24 * 60 * 60 * 1000)
    public void refreshMovieData() {
        List<Map<String, Object>> movies = tmdbService.getCombinedUniqueMovies();

        try {
            for (Map<String, Object> movieData : movies) {
                if (movieRepository.existsByTmdbId((Long) movieData.get("tmdbId"))) {
                    continue;
                }

                Object genresObj = movieData.get("genres");
                List<String> genres = null;
                if (genresObj instanceof List<?>) {
                    genres = ((List<?>) genresObj).stream()
                            .map(Object::toString)
                            .toList();
                }

                Movie movie = Movie.builder()
                    .tmdbId((Long) movieData.get("tmdbId"))
                    .title((String) movieData.get("title"))
                    .description((String) movieData.get("description"))
                    .posterPath((String) movieData.get("posterPath"))
                    .genres(genres)
                    .build();

                movieRepository.save(movie);
            }
            System.out.println("Movie data refreshed successfully.");
        } catch (Exception e) {
            System.err.println("Error during movie data refresh: " + e.getMessage());
        }

    }
}