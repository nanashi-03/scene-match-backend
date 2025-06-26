package com.silveredsoul.scene_match.config;

import com.silveredsoul.scene_match.service.MovieDataRefreshService;
import com.silveredsoul.scene_match.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner preloadMovies(MovieDataRefreshService movieDataRefreshService, MovieRepository repo) {
        return args -> {
            repo.deleteAll();
            movieDataRefreshService.refreshMovieData();
        };
    }
}
