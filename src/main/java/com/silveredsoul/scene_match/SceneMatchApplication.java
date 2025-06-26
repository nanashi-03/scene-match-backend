package com.silveredsoul.scene_match;

import com.silveredsoul.scene_match.model.User;
import com.silveredsoul.scene_match.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SceneMatchApplication {

    @Value("${admin.password}")
    private String rawPassword;

    @Value("${admin.username}")
    private String username;

    public static void main(String[] args) {
        SpringApplication.run(SceneMatchApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "db.init.enabled", havingValue = "true")
    CommandLineRunner runner(UserRepository userRepo, PasswordEncoder encoder) {
        String password = encoder.encode(rawPassword);

        return args -> {
            userRepo.save(User.builder()
                .username(username)
                .email("11052003ac@gmail.com")
                .password(password)
                .preferences(List.of("action", "sci-fi", "thriller"))
                .build()
            );
        };
    }

}
