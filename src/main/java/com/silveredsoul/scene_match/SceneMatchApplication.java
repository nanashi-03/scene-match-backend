package com.silveredsoul.scene_match;

import com.silveredsoul.scene_match.model.Item;
import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.ItemRepository;
import com.silveredsoul.scene_match.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class SceneMatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SceneMatchApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "db.init.enabled", havingValue = "true")
    CommandLineRunner runner(UserRepository userRepo, ItemRepository itemRepo, PasswordEncoder encoder) {
        String password = encoder.encode("zaqwsx12");

        return args -> {
            userRepo.save(new User(null, "Nanashi", "11052003ac@gmail.com", password, List.of("action", "sci-fi", "thriller")));

            itemRepo.save(new Item(null, "Inception", "movie", 9.0, List.of("sci-fi", "thriller")));
            itemRepo.save(new Item(null, "Avengers", "movie", 8.5, List.of("action", "superhero")));
            itemRepo.save(new Item(null, "Notebook", "movie", 7.5, List.of("romance", "drama")));
        };
    }
}
