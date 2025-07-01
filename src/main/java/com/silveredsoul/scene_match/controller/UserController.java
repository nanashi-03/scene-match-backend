package com.silveredsoul.scene_match.controller;

import com.silveredsoul.scene_match.service.PreferenceService;
import com.silveredsoul.scene_match.service.RecommendationService;
import com.silveredsoul.scene_match.data.UpdatePreferencesRequest;
import com.silveredsoul.scene_match.data.UserProfileResponse;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;

import java.util.Map;
import java.util.Set;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.RequestBody;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/{userId}")
@RequiredArgsConstructor
public class UserController {
    private final PreferenceService preferenceService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;

    @PostMapping("/like/{tmdbId}")
    public ResponseEntity<String> likeMovie(@PathVariable Long userId, @PathVariable Long tmdbId) {
        preferenceService.likeMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie liked");
    }
    
    @PostMapping("/watch/{tmdbId}")
    public ResponseEntity<String> watchMovie(@PathVariable Long userId, @PathVariable Long tmdbId) {
        preferenceService.watchMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie watched");
    }

    @PostMapping("/dislike/{tmdbId}")
    public ResponseEntity<String> dislikeMovie(@PathVariable Long userId, @PathVariable Long tmdbId) {
        preferenceService.dislikeMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie disliked");
    }

    @PostMapping("/unwatch/{tmdbId}")
    public ResponseEntity<String> unwatchMovie(@PathVariable Long userId, @PathVariable Long tmdbId) {
        preferenceService.unwatchMovie(userId, tmdbId);
        return ResponseEntity.ok("Movie unwatched");
    }

    @PutMapping("/preferences")
    public ResponseEntity<String> updatePreferences(@PathVariable Long userId, @RequestBody UpdatePreferencesRequest request) {
        preferenceService.updatePreferences(userId, request);
        return ResponseEntity.ok("User preferences updated");
    }

    @DeleteMapping("/preferences")
    public ResponseEntity<String> deletePreferences(@PathVariable Long userId, @RequestBody UpdatePreferencesRequest request) {
        preferenceService.deletePreferences(userId, request);
        return ResponseEntity.ok("User preferences modified");
    }

    @GetMapping("/preferences")
    public ResponseEntity<UpdatePreferencesRequest> getUserPreferences(@PathVariable Long userId) {
        UpdatePreferencesRequest preferences = preferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/recommendations")
    public List<Movie> getRecommendations(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return recommendationService.recommendMovies(user);
    }

    @GetMapping("/liked")
    public Set<Long> getLikedMovies(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getLikedMovies();
    }

    @GetMapping("/watched")
    public Set<Long> getWatchedMovies(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getWatchedMovies();
    }

    @GetMapping()
    public UserProfileResponse getUserProfile(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Map<String, Set<String>> genrePreferences = user.getGenrePreferences();
        Map<String, Set<String>> keywordPreferences = user.getKeywordPreferences();

        UserProfileResponse userProfile = UserProfileResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .genrePreferences(genrePreferences)
            .keywordPreferences(keywordPreferences)
            .likedMovies(user.getLikedMovies())
            .watchedMovies(user.getWatchedMovies())
            .role(user.getRole())
            .build();
        return userProfile;
    }

}