package com.silveredsoul.scene_match.repository;

import com.silveredsoul.scene_match.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>{
    Optional<Item> findByTitle(String title);
    
    boolean existsByTitle(String title);
    
    boolean existsByCategory(String category);
    
    boolean existsByRating(Double rating);
    
    boolean existsByTagsContaining(String tag);
}