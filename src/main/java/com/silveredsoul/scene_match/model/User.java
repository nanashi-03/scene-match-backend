package com.silveredsoul.scene_match.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Data;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "AppUser")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NonNull
    private String username;

    @Email
    private String email;

    @NonNull
    private String password;

    @ElementCollection
    @CollectionTable(name = "user_genre_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "preference_type") // "likes" or "dislikes"
    @Column(name = "genre")
    @Builder.Default
    private Map<String, Set<String>> genrePreferences = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "user_keyword_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "preference_type") // "likes" or "dislikes"
    @Column(name = "keywords")
    @Builder.Default
    private Map<String, Set<String>> keywordPreferences = new HashMap<>();

    @ElementCollection
    @Builder.Default
    private Set<Long> likedMovies = new HashSet<>();

    @ElementCollection
    @Builder.Default
    private Set<Long> watchedMovies = new HashSet<>();

    @Builder.Default
    private String role = "ROLE_USER";

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(String.format("ROLE_%s", role)));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
