package com.silveredsoul.scene_match.service;

import com.silveredsoul.scene_match.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private final TokenBlacklistService blacklistService;

    public String refreshToken(String token) {
        Claims claims = getClaims(token);
        return generateTokenFromClaims(claims);
    }

    private String generateTokenFromClaims(Claims claims) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.builder()
            .claims(claims)
            .subject(claims.getSubject())
            .expiration(exp)
            .issuedAt(now)
            .signWith(key)
            .compact();
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expiration);
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder().subject(user.getUsername())
            .claim("userId", user.getId())
            .expiration(exp)
            .issuedAt(now)
            .signWith(key)
            .compact();
    }

    public String extractUsername(String token) {
        try {
            return getClaims(token).getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public Long extractId(String token) {
        try {
            return Long.valueOf((Integer)(getClaims(token).get("userId")));
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isValid(String token, User user) {
        if (blacklistService.isBlacklisted(token))
            return false;
        String username = extractUsername(token);
        return username != null && username.equals(user.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        try {
            return getClaims(token).getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }
    }

    private Claims getClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        
        try {
            return Jwts.parser().verifyWith(key)
                .build().parseSignedClaims(token).getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw e;
        }
    }
}