package com.silveredsoul.scene_match.service;

import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MovieRepository movieRepository;

    public List<Movie> recommendMovies(User user) {
        Set<Long> watchedMovieIds = user.getWatchedMovies();
        List<Movie> allMovies = movieRepository.findAll().stream()
            .filter(movie -> movie.getTmdbId() != null && !watchedMovieIds.contains(movie.getTmdbId()))
            .toList();
        LocalDate cutoffDate = LocalDate.now().minusYears(2);

        Map<Movie, Integer> scoredMovies = new HashMap<>();
        Set<String> likedGenres = user.getGenrePreferences().getOrDefault("likes", new HashSet<>());
        Set<String> dislikedGenres = user.getGenrePreferences().getOrDefault("dislikes", new HashSet<>());
        Set<String> likedKeywords = user.getKeywordPreferences().getOrDefault("likes", new HashSet<>());
        Set<String> dislikedKeywords = user.getKeywordPreferences().getOrDefault("dislikes", new HashSet<>());

        for (Movie movie : allMovies) {
            int score = 0;

            // Liked Genres (+3)
            if (movie.getGenres() != null && likedGenres != null && dislikedGenres != null) {
                for (String genre : movie.getGenres()) {
                    if (likedGenres.contains(genre)) {
                        score += 3;
                    }
                    if (dislikedGenres.contains(genre)) {
                        score -= 4;
                    }
                }
            }

            // Liked Keywords (+1), Disliked Keywords (-2)
            if (movie.getKeywords() != null && likedKeywords != null && dislikedKeywords != null) {
                for (String keyword : movie.getKeywords()) {
                    if (likedKeywords.contains(keyword)) {
                        score += 1;
                    }
                    if (dislikedKeywords.contains(keyword)) {
                        score -= 2;
                    }
                }
            }

            // Top Rated (+1)
            if (movie.getTopRated()) {
                score += 2;
            }

            // Recently Released (+1)
            if (movie.getReleaseDate() != null && movie.getReleaseDate().isAfter(cutoffDate)) {
                score += 1;
            }
            
            scoredMovies.put(movie, score);
        }

        // Sort descending by score
        return scoredMovies.entrySet().stream()
                .sorted(Map.Entry.<Movie, Integer>comparingByValue().reversed())
                .limit(20)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
