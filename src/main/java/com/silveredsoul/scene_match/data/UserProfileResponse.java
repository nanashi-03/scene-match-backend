package com.silveredsoul.scene_match.data;

import java.util.Set;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private Long id;
    private String username;
    private String email;
    private Map<String, Set<String>> genrePreferences;
    private Map<String, Set<String>> keywordPreferences;
    private Set<Long> likedMovies;
    private Set<Long> watchedMovies;
    private String role;
}