package com.silveredsoul.scene_match.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.silveredsoul.scene_match.model.Movie;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
// import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        Map.entry(28, "action"),
        Map.entry(12, "adventure"),
        Map.entry(16, "animation"),
        Map.entry(35, "comedy"),
        Map.entry(80, "crime"),
        Map.entry(99, "documentary"),
        Map.entry(18, "drama"),
        Map.entry(10751, "family"),
        Map.entry(14, "fantasy"),
        Map.entry(36, "history"),
        Map.entry(27, "horror"),
        Map.entry(10402, "music"),
        Map.entry(9648, "mystery"),
        Map.entry(10749, "romance"),
        Map.entry(878, "science fiction"),
        Map.entry(10770, "tv movie"),
        Map.entry(53, "thriller"),
        Map.entry(10752, "war"),
        Map.entry(37, "western")
    );

    private List<String> getKeywordsNode(org.springframework.http.HttpEntity<String> entity, Long tmdbId)
            throws InterruptedException {
        String keywordsUrl = String.format("%s/movie/%d/keywords?api_key=%s", baseUrl,
                tmdbId, apiKey);
        List<String> keywords = new ArrayList<>();
        try {
            org.springframework.http.ResponseEntity<JsonNode> keywordsResponse = restTemplate.exchange(
                    keywordsUrl, org.springframework.http.HttpMethod.GET, entity, JsonNode.class);
            Thread.sleep(25);
            JsonNode keywordsNode = keywordsResponse.getBody();
            if (keywordsNode != null && keywordsNode.has("keywords")) {
                for (JsonNode keywordNode : keywordsNode.get("keywords")) {
                    keywords.add(keywordNode.get("name").asText().toLowerCase());
                }
            }
        } catch (RestClientException e) {
            System.err.println("Failed to fetch keywords for TMDB ID " + tmdbId + ": " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Interrupted while fetching keywords for TMDB ID " + tmdbId + ": " + e.getMessage());
        }
        return keywords;
    }

    // "popular" and "top_rated" categories
    private List<Movie> getMovies(String category, int page) {
        List<Movie> allMovies = new ArrayList<>();
    
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
    
        String url = String.format("%s/movie/%s?page=%d", baseUrl, category, page);
        System.out.println(url);

        try {
            org.springframework.http.ResponseEntity<JsonNode> responseEntity =
                    restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, entity, JsonNode.class);
            Thread.sleep(25);
            JsonNode response = responseEntity.getBody();
            if (response != null && response.has("results")) {
                for (JsonNode result : response.get("results")) {
                    List<String> genres = new ArrayList<>();
                    List<String> keywords = new ArrayList<>();

                    for (JsonNode genreIdNode : result.get("genre_ids")) {
                        int genreId = genreIdNode.asInt();
                        String genreName = GENRE_MAP.getOrDefault(genreId, "Unknown");
                        genres.add(genreName);
                    }
                    Long tmdbId = result.get("id").asLong();
                    keywords = getKeywordsNode(entity, tmdbId);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String releaseDateStr = result.has("release_date") ? result.get("release_date").asText() : null;
                    LocalDate releaseDate = LocalDate.parse(releaseDateStr, formatter);

                    
                    Movie movie = Movie.builder()
                            .tmdbId(tmdbId)
                            .title(result.get("title").asText())
                            .description(result.get("overview").asText())
                            .posterPath("https://image.tmdb.org/t/p/original"+result.get("poster_path").asText())
                            .genres(genres)
                            .keywords(keywords)
                            .releaseDate(releaseDate)
                            .topRated(false)
                            .build();

                    allMovies.add(movie);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch page " + page + " of " + category + ": " + e.getMessage());
        }
    
        return allMovies;
    }

    
    
    public List<Movie> getCombinedUniqueMovies() {
        List<Movie> combined = new ArrayList<>();
        Set<Long> seenIds = new HashSet<>();
        int pages = 2;
        List<Movie> popular = new ArrayList<>();
        List<Movie> topRated = new ArrayList<>();
        for (int page = 1; page <= pages; page++) {
            popular.addAll(getMovies("popular", page));
            topRated.addAll(getMovies("top_rated", page));
        }

        for (Movie movie : popular) {
            Long id = (Long) movie.getTmdbId();
            if (seenIds.add(id)) {
                combined.add(movie);
            }
        }

        for (Movie movie : topRated) {
            Long id = (Long) movie.getTmdbId();
            movie.setTopRated(true); // Set TopRated to true for top-rated movies
            if (seenIds.add(id)) {
                combined.add(movie);
            } else {
                combined.stream()
                        .filter(m -> m.getTmdbId().equals(id))
                        .findFirst()
                        .ifPresent(existingMovie -> {
                            // Chagne the value of TopRated to true if it is not already
                            if (existingMovie.getTopRated() == null || !existingMovie.getTopRated()) {
                                existingMovie.setTopRated(true);
                            }
                        });
            }
        }

        return combined;
    }

    // Get movie by Id
    public Movie getMovieById(Long tmdbId) {
        String url = String.format("%s/movie/%d?api_key=%s&language=en-US", baseUrl, tmdbId, apiKey);
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setBearerAuth(accessToken);
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(headers);
        try {
            org.springframework.http.ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(url,
                    org.springframework.http.HttpMethod.GET, entity, JsonNode.class);
            Thread.sleep(25);
            JsonNode movieNode = responseEntity.getBody();
            if (movieNode != null) {
                List<String> genres = new ArrayList<>();
                for (JsonNode genre : movieNode.get("genres")) {
                    genres.add(genre.get("name").asText().toLowerCase());
                }

                List<String> keywords = new ArrayList<>();
                String keywordsUrl = String.format("%s/movie/%d/keywords?api_key=%s", baseUrl, tmdbId, apiKey);
                org.springframework.http.ResponseEntity<JsonNode> keywordsResponse = restTemplate.exchange(
                        keywordsUrl, org.springframework.http.HttpMethod.GET, entity, JsonNode.class);
                JsonNode keywordsNode = keywordsResponse.getBody();
                if (keywordsNode != null && keywordsNode.has("keywords")) {
                    for (JsonNode keywordNode : keywordsNode.get("keywords")) {
                        keywords.add(keywordNode.get("name").asText());
                    }
                }

                Movie movie = Movie.builder()
                        .tmdbId(movieNode.get("id").asLong())
                        .title(movieNode.get("title").asText())
                        .description(movieNode.get("overview").asText())
                        .posterPath(movieNode.get("poster_path").asText())
                        .genres(genres)
                        .keywords(keywords)
                        .build();

                return movie;
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch movie with ID " + tmdbId + ": " + e.getMessage());
        }
        return null;
    }
}