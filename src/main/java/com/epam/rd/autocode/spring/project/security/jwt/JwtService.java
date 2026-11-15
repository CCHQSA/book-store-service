package com.epam.rd.autocode.spring.project.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt_secret}")
    private String jwtSecret;

    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000L;

    @PostConstruct
    public void validateSecret() {
        if (jwtSecret == null || jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 bytes");
        }
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(getSignKey())
                .compact();
    }

    public boolean isTokenValid(UserDetails userDetails, String token) {
        try {
            String tokenUsername = extractUserName(token);
            return tokenUsername != null && tokenUsername.equals(userDetails.getUsername());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractUserName(String token) {
        return extractAllClaims(token).getSubject();
    }

    private SecretKey getSignKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
