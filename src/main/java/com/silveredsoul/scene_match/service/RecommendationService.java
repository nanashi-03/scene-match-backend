package com.silveredsoul.scene_match.service;

import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    public RecommendationService(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public List<Movie> recommendForUser(Long userId) {
        try {
            User user = userRepository.findById(userId).orElseThrow();
            List<String> userGenres = user.getPreferences();

            // Recommend movies that match at least one of the user's favorite genres,
            // and sort them by rating descending
            return movieRepository.findAll().stream()
                    .filter(movie -> !Collections.disjoint(movie.getGenres(), userGenres))
                    .limit(10)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
