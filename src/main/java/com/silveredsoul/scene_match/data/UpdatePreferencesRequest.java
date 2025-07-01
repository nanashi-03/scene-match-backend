package com.silveredsoul.scene_match.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePreferencesRequest {
    private String likedGenre;
    private String dislikedGenre;
    private String likedKeyword;
    private String dislikedKeyword;
}