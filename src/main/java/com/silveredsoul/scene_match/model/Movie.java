package com.silveredsoul.scene_match.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tmdbId;

    private String title;
    @Column(length = 2000)
    private String description;

    private String posterPath;

    @ElementCollection
    private List<String> genres;

    @ElementCollection
    private List<String> tags;
}
