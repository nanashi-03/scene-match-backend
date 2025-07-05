package com.silveredsoul.scene_match.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPreferencesResponse {
    private List<String> likedGenres;
    private List<String> dislikedGenres;
    private List<String> likedKeywords;
    private List<String> dislikedKeywords;
}