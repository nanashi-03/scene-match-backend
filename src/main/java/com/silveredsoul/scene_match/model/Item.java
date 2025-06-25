package com.silveredsoul.scene_match.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;
    private Double rating;

    @ElementCollection
    private List<String> tags;

    // public Item() {
    // }

    // public Item(Long id, String title, String category, Double rating, List<String> tags) {
    //     this.id = id;
    //     this.title = title;
    //     this.category = category;
    //     this.rating = rating;
    //     this.tags = tags;
    // }
    
    // // Getters and setters
    // public String getTitle() {
    //     return title;
    // }

    // public void setTitle(String title) {
    //     this.title = title;
    // }

    // public String getCategory() {
    //     return category;
    // }

    // public void setCategory(String category) {
    //     this.category = category;
    // }

    // public Double getRating() {
    //     return rating;
    // }

    // public void setRating(Double rating) {
    //     this.rating = rating;
    // }
    
    // public List<String> getTags() {
    //     return tags;
    // }

    // public void setTags(List<String> tags) {
    //     this.tags = tags;
    // }
}
