package com.silveredsoul.scene_match.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchRequest {
    private String title;
    private List<String> genres;
    private List<String> keywords;
}
