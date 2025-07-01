package com.silveredsoul.scene_match.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.silveredsoul.scene_match.model.Movie;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieSearchService {
    public Specification<Movie> advancedSearch(
            String title,
            List<String> genres,
            List<String> keywords) {
        return (Root<Movie> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Title mandatory
            if (title != null && !title.isBlank()) {
                // predicates.add(cb.like(root.get("title"), title));
                predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            } else {
                throw new IllegalArgumentException("Title must not be empty");
            }

            // Genres optional
            if (genres != null && !genres.isEmpty()) {
                Join<Movie, String> genreJoin = root.join("genres", JoinType.LEFT);
                predicates.add(genreJoin.in(genres));
            }

            // Keywords optional
            if (keywords != null && !keywords.isEmpty()) {
                Join<Movie, String> keywordJoin = root.join("keywords", JoinType.LEFT);
                predicates.add(keywordJoin.in(keywords));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
