package com.silveredsoul.scene_match.controller;

import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.repository.MovieRepository;
// import com.silveredsoul.scene_match.service.TmdbService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    // private final TmdbService tmdbService;
    private final MovieRepository movieRepo;

    // @PostMapping("/import")
    // public String importPopularMovies() {
    //     List<Map<String, Object>> tmdbMovies = tmdbService.getCombinedUniqueMovies();
    //     int added = 0;

    //     for (Map<String, Object> tmdbMovie : tmdbMovies) {
    //         String title = (String) tmdbMovie.get("title");
    //         Movie movie = new Movie();
    //         movie.setTitle(title);
    //         movie.setDescription((String) tmdbMovie.get("description"));
    //         movie.setPosterPath((String) tmdbMovie.get("posterPath"));
    //         Object genresObj = tmdbMovie.get("genres");
    //         List<String> genres = new ArrayList<>();
    //         if (genresObj instanceof List<?>) {
    //             for (Object genre : (List<?>) genresObj) {
    //                 if (genre instanceof String) {
    //                     genres.add((String) genre);
    //                 }
    //             }
    //         }
    //         movie.setGenres(genres);
    //         movie.setTags(new ArrayList<>());
    //         movie.setLikeCount(0);
    //         movieRepo.save(movie);
    //         added++;
    //     }
    //     return added + " movies imported successfully";
    // }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepo.findAll();
    }
}
