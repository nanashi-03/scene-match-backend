package com.silveredsoul.scene_match.controller;

import com.silveredsoul.scene_match.model.Item;
import com.silveredsoul.scene_match.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public List<Item> getRecommendations(@PathVariable Long userId) {
        return recommendationService.recommendForUser(userId);
    }
}