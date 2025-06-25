package com.silveredsoul.scene_match.service;

import com.silveredsoul.scene_match.model.Item;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.ItemRepository;
import com.silveredsoul.scene_match.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final ItemRepository itemRepo;
    private final UserRepository userRepo;

    public RecommendationService(ItemRepository itemRepo, UserRepository userRepo) {
        this.itemRepo = itemRepo;
        this.userRepo = userRepo;
    }

    public List<Item> recommendForUser(Long userId) {
        try {
            User user = userRepo.findById(userId).orElseThrow();
            List<String> prefs = user.getPreferences();

            return itemRepo.findAll().stream()
                    .filter(item -> !Collections.disjoint(item.getTags(), prefs))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
