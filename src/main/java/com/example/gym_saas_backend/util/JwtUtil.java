package com.example.gym_saas_backend.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Updated: Now also includes gymOwnerId
    public String generateToken(String email, String role, Long gymOwnerId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("gymOwnerId", gymOwnerId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60L * 24 * 60 * 60 * 1000)) // 21 days
                .signWith(key)
                .compact();
    }

    public Long extractGymOwnerId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("gymOwnerId", Long.class);
    }


    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return getClaims(token).get("role", String.class);
    }

    // ✅ New method to extract gymOwnerId
//    public Long extractGymOwnerId(String token) {
//        return getClaims(token).get("gymOwnerId", Long.class);
//    }

    public boolean validateToken(String token) {
        try {
            getClaims(token); // Just to validate the structure and signature
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // 🔐 Helper method to avoid repetition
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsernameFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return extractUsername(token);
        }
        return null;
    }

}
