package com.silveredsoul.scene_match.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "movies",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tmdbId"}))
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tmdbId;
    private String title;

    @Column(length = 2000)
    private String description;

    private String posterPath;

    private LocalDate releaseDate;

    private Boolean topRated;

    @ElementCollection
    private List<String> genres;

    @ElementCollection
    private List<String> keywords;
}
