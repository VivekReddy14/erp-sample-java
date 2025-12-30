package com.example.demo.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtService {

    private final Key key;
    private final long expiryMinutes;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiryMinutes}") long expiryMinutes
    ) {
        if (secret == null || secret.trim().length() < 32) {
            throw new IllegalArgumentException("app.jwt.secret must be at least 32 characters");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiryMinutes = expiryMinutes;
    }

    public String generateToken(Long userId, String username, Long employeeId, List<String> authorities) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expiryMinutes * 60);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .addClaims(Map.of(
                        "username", username,
                        "employeeId", employeeId,
                        "authorities", authorities
                ))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public long getExpiryEpochSecondsFromNow() {
        return Instant.now().plusSeconds(expiryMinutes * 60).getEpochSecond();
    }
}
