package com.silveredsoul.scene_match.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final RestTemplate restTemplate;

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.access.token}")
    private String accessToken;

    @Value("${tmdb.base.url}")
    private String baseUrl;

    private final static Map<Integer, String> GENRE_MAP = Map.ofEntries(
            Map.entry(28, "Action"),
            Map.entry(12, "Adventure"),
            Map.entry(16, "Animation"),
            Map.entry(35, "Comedy"),
            Map.entry(80, "Crime"),
            Map.entry(99, "Documentary"),
            Map.entry(18, "Drama"),
            Map.entry(10751, "Family"),
            Map.entry(14, "Fantasy"),
            Map.entry(36, "History"),
            Map.entry(27, "Horror"),
            Map.entry(10402, "Music"),
            Map.entry(9648, "Mystery"),
            Map.entry(10749, "Romance"),
            Map.entry(878, "Science Fiction"),
            Map.entry(10770, "TV Movie"),
            Map.entry(53, "Thriller"),
            Map.entry(10752, "War"),
            Map.entry(37, "Western"));

    private List<Map<String, Object>> getPopularMovies() {
        List<Map<String, Object>> allMovies = new ArrayList<>();
    
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
    
        for (int page = 1; page <= 50; page++) {
            String url = String.format("%s/movie/popular?page=%d", baseUrl, page);
    
            try {
                org.springframework.http.ResponseEntity<JsonNode> responseEntity =
                        restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, JsonNode.class);
    
                JsonNode response = responseEntity.getBody();
                if (response != null && response.has("results")) {
                    for (JsonNode result : response.get("results")) {
                        Map<String, Object> movie = new HashMap<>();
                        movie.put("tmdbId", result.get("id").asLong());
                        movie.put("title", result.get("title").asText());
                        movie.put("description", result.get("overview").asText());
                        movie.put("posterPath", result.get("poster_path").asText());
    
                        List<String> genres = new ArrayList<>();
                        for (JsonNode genreIdNode : result.get("genre_ids")) {
                            int genreId = genreIdNode.asInt();
                            String genreName = GENRE_MAP.getOrDefault(genreId, "Unknown");
                            genres.add(genreName);
                        }
    
                        movie.put("genres", genres);
                        allMovies.add(movie);
                    }
                }

                Thread.sleep(20);
            } catch (Exception e) {
                System.err.println("Failed to fetch page " + page + ": " + e.getMessage());
            }
        }
    
        return allMovies;
    }

    private List<Map<String, Object>> getTopRatedMovies() {
        List<Map<String, Object>> allMovies = new ArrayList<>();

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);

        for (int page = 1; page <= 50; page++) {
            String url = String.format("%s/movie/top_rated?page=%d", baseUrl, page);

            try {
                org.springframework.http.ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url,
                        org.springframework.http.HttpMethod.GET, entity, JsonNode.class);

                JsonNode response = responseEntity.getBody();
                if (response != null && response.has("results")) {
                    for (JsonNode result : response.get("results")) {
                        Map<String, Object> movie = new HashMap<>();
                        movie.put("tmdbId", result.get("id").asLong());
                        movie.put("title", result.get("title").asText());
                        movie.put("description", result.get("overview").asText());
                        movie.put("posterPath", result.get("poster_path").asText());

                        List<String> genres = new ArrayList<>();
                        for (JsonNode genreIdNode : result.get("genre_ids")) {
                            int genreId = genreIdNode.asInt();
                            String genreName = GENRE_MAP.getOrDefault(genreId, "Unknown");
                            genres.add(genreName);
                        }

                        movie.put("genres", genres);
                        allMovies.add(movie);
                    }
                }

                Thread.sleep(20); // Respect TMDB rate limit
            } catch (Exception e) {
                System.err.println("Failed to fetch top-rated page " + page + ": " + e.getMessage());
            }
        }

        return allMovies;
    }
    
    public List<Map<String, Object>> getCombinedUniqueMovies() {
        List<Map<String, Object>> combined = new ArrayList<>();
        Set<Long> seenIds = new HashSet<>();

        List<Map<String, Object>> popular = getPopularMovies();
        List<Map<String, Object>> topRated = getTopRatedMovies();

        for (Map<String, Object> movie : popular) {
            Long id = (Long) movie.get("tmdbId");
            if (seenIds.add(id)) {
                combined.add(movie);
            }
        }

        for (Map<String, Object> movie : topRated) {
            Long id = (Long) movie.get("tmdbId");
            if (seenIds.add(id)) {
                combined.add(movie);
            }
        }

        return combined;
    }
    
}