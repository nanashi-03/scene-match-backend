package com.silveredsoul.scene_match.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.silveredsoul.scene_match.data.UpdatePreferencesRequest;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final UserRepository userRepo;

    public User updatePreferences(Long userId, UpdatePreferencesRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<String> likedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("dislikes", new HashSet<>()));
        Set<String> likedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("dislikes", new HashSet<>()));

        if (request.getLikedGenre() != null) {
            likedGenres.add(request.getLikedGenre());
        }
        if (request.getDislikedGenre() != null) {
            dislikedGenres.add(request.getDislikedGenre());
        }
        if (request.getLikedKeyword() != null) {
            likedKeywords.add(request.getLikedKeyword());
        }
        if (request.getDislikedKeyword() != null) {
            dislikedKeywords.add(request.getDislikedKeyword());
        }
        Map<String, Set<String>> genrePreferences = new HashMap<>();
        genrePreferences.put("likes", likedGenres);
        genrePreferences.put("dislikes", dislikedGenres);
        user.setGenrePreferences(genrePreferences);
        Map<String, Set<String>> keywordPreferences = new HashMap<>();
        keywordPreferences.put("likes", likedKeywords);
        keywordPreferences.put("dislikes", dislikedKeywords);
        user.setKeywordPreferences(keywordPreferences);

        return userRepo.save(user);
    }

    public void deletePreferences(Long userId, UpdatePreferencesRequest request) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Set<String> likedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("dislikes", new HashSet<>()));
        Set<String> likedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("dislikes", new HashSet<>()));

        if (request.getLikedGenre() != null) {
            likedGenres.remove(request.getLikedGenre());
        }
        if (request.getDislikedGenre() != null) {
            dislikedGenres.remove(request.getDislikedGenre());
        }
        if (request.getLikedKeyword() != null) {
            likedKeywords.remove(request.getLikedKeyword());
        }
        if (request.getDislikedKeyword() != null) {
            dislikedKeywords.remove(request.getDislikedKeyword());
        }
        Map<String, Set<String>> genrePreferences = new HashMap<>();
        genrePreferences.put("likes", likedGenres);
        genrePreferences.put("dislikes", dislikedGenres);
        user.setGenrePreferences(genrePreferences);
        Map<String, Set<String>> keywordPreferences = new HashMap<>();
        keywordPreferences.put("likes", likedKeywords);
        keywordPreferences.put("dislikes", dislikedKeywords);
        user.setKeywordPreferences(keywordPreferences);

        userRepo.save(user);
    }

    public UpdatePreferencesRequest getUserPreferences(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Set<String> likedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedGenres = new HashSet<>(
                user.getGenrePreferences().getOrDefault("dislikes", new HashSet<>()));
        Set<String> likedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("likes", new HashSet<>()));
        Set<String> dislikedKeywords = new HashSet<>(
                user.getKeywordPreferences().getOrDefault("dislikes", new HashSet<>()));

        return new UpdatePreferencesRequest(
            likedGenres.isEmpty() ? null : String.join(", ", likedGenres),
            dislikedGenres.isEmpty() ? null : String.join(", ", dislikedGenres),
            likedKeywords.isEmpty() ? null : String.join(", ", likedKeywords),
            dislikedKeywords.isEmpty() ? null : String.join(", ", dislikedKeywords)
        );
    }

    public void watchMovie(Long userId, Long tmdbMovieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getWatchedMovies().add(tmdbMovieId);
        userRepo.save(user);
    }

    public void unwatchMovie(Long userId, Long tmdbMovieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getLikedMovies().remove(tmdbMovieId);
        user.getWatchedMovies().remove(tmdbMovieId);
        userRepo.save(user);
    }

    public void likeMovie(Long userId, Long tmdbMovieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getWatchedMovies().add(tmdbMovieId);
        user.getLikedMovies().add(tmdbMovieId);
        userRepo.save(user);
    }

    public void dislikeMovie(Long userId, Long tmdbMovieId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        user.getLikedMovies().remove(tmdbMovieId);
        userRepo.save(user);
    }
}
