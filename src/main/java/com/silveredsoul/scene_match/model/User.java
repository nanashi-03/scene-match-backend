package com.silveredsoul.scene_match.model;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "AppUser")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String username;
    private String email;

    @NonNull
    private String password;
    
    @ElementCollection
    private List<String> preferences;

    // public User() {
    // }

    // public User(Long id, String username, String email, List<String> preferences) {
    //     this.id = id;
    //     this.username = username;
    //     this.email = email;
    //     this.preferences = preferences;
    // }

    // // Getters and setters
    // public String getUsername() {
    //     return username;
    // }

    // public void setUsername(String username) {
    //     this.username = username;
    // }

    // public String getEmail() {
    //     return email;
    // }

    // public void setEmail(String email) {
    //     this.email = email;
    // }

    // public List<String> getPreferences() {
    //     return preferences;
    // }

    // public void setPreferences(List<String> preferences) {
    //     this.preferences = preferences;
    // }
}
