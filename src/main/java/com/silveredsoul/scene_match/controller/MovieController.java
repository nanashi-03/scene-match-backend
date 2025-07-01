package com.silveredsoul.scene_match.controller;

// import com.silveredsoul.scene_match.data.MovieSearchRequest;
import com.silveredsoul.scene_match.model.Movie;
import com.silveredsoul.scene_match.repository.MovieRepository;
// import com.silveredsoul.scene_match.service.MovieSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
// import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieRepository movieRepo;
    // private final MovieSearchService movieSearchService;

    @GetMapping
    public List<Movie> getAllMovies(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        return movieRepo.findAll(pageable).getContent();
    }

    @GetMapping("/{tmdbId}")
    public Movie getMovieById(@PathVariable Long tmdbId) {
        return movieRepo.findByTmdbId(tmdbId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }

    // @PostMapping("/search")
    // public List<Movie> searchMovies(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int size, @RequestBody MovieSearchRequest request) 
    // {
    //     Pageable pageable = PageRequest.of(page, size);
    //     return movieRepo.findAll(
    //             movieSearchService.advancedSearch(
    //                     request.getTitle(),
    //                     request.getGenres(),
    //                     request.getKeywords()),
    //             pageable).getContent();
    // }
}
