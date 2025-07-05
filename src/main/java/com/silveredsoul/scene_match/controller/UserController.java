package com.silveredsoul.scene_match.controller;

import com.silveredsoul.scene_match.service.JwtService;
import com.silveredsoul.scene_match.service.PreferenceService;
import com.silveredsoul.scene_match.service.RecommendationService;


import com.silveredsoul.scene_match.data.UpdatePreferencesRequest;
import com.silveredsoul.scene_match.data.UserPreferencesResponse;
import com.silveredsoul.scene_match.data.UserProfileResponse;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;
import com.silveredsoul.scene_match.data.Responses.MessageResponse;

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
import org.springframework.web.bind.annotation.RequestHeader;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final PreferenceService preferenceService;
    private final RecommendationService recommendationService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/like/{tmdbId}")
    public ResponseEntity<MessageResponse> likeMovie(@RequestHeader("Authorization") String authHeader, @PathVariable Long tmdbId) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.likeMovie(userId, tmdbId);
        return ResponseEntity.ok(new MessageResponse("Movie liked"));
    }
    
    @PostMapping("/watch/{tmdbId}")
    public ResponseEntity<MessageResponse> watchMovie(@RequestHeader("Authorization") String authHeader, @PathVariable Long tmdbId) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.watchMovie(userId, tmdbId);
        return ResponseEntity.ok(new MessageResponse("Movie watched"));
    }

    @PostMapping("/dislike/{tmdbId}")
    public ResponseEntity<MessageResponse> dislikeMovie(@RequestHeader("Authorization") String authHeader, @PathVariable Long tmdbId) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.dislikeMovie(userId, tmdbId);
        return ResponseEntity.ok(new MessageResponse("Movie disliked"));
    }

    @PostMapping("/unwatch/{tmdbId}")
    public ResponseEntity<MessageResponse> unwatchMovie(@RequestHeader("Authorization") String authHeader, @PathVariable Long tmdbId) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.unwatchMovie(userId, tmdbId);
        return ResponseEntity.ok(new MessageResponse("Movie unwatched"));
    }

    @PutMapping("/preferences")
    public ResponseEntity<MessageResponse> updatePreferences(@RequestHeader("Authorization") String authHeader, @RequestBody UpdatePreferencesRequest request) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.updatePreferences(userId, request);
        return ResponseEntity.ok(new MessageResponse("User preferences updated"));
    }

    @DeleteMapping("/preferences")
    public ResponseEntity<MessageResponse> deletePreferences(@RequestHeader("Authorization") String authHeader, @RequestBody UpdatePreferencesRequest request) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        preferenceService.deletePreferences(userId, request);
        return ResponseEntity.ok(new MessageResponse("User preferences modified"));
    }

    @GetMapping("/preferences")
    public ResponseEntity<UserPreferencesResponse> getUserPreferences(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        UserPreferencesResponse preferences = preferenceService.getUserPreferences(userId);
        return ResponseEntity.ok(preferences);
    }

    @GetMapping("/recommendations")
    public List<Movie> getRecommendations(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return recommendationService.recommendMovies(user);
    }

    @GetMapping("/liked")
    public Set<Long> getLikedMovies(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getLikedMovies();
    }

    @GetMapping("/watched")
    public Set<Long> getWatchedMovies(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return user.getWatchedMovies();
    }

    @GetMapping("/profile")
    public UserProfileResponse getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        Long userId = jwtService.extractId(token);
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