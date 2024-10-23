package com.example.shopapp.component;

import com.example.shopapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils {

    private static final int EXPIRATION = 2592000; // 2592000 seconds
    private static final String SECRET_KEY = "sOmeV3ry53cureKey1234567890123456"; // Secret key

    // Generate JWT token
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("userId",user.getId());
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new JwtException("Cannot create JWT token: " + e.getMessage());
        }
    }

    // Get signing key from secret key
    private Key getSignInKey() {
        if (SECRET_KEY.length() < 32) {
            throw new IllegalArgumentException("Secret key must be at least 32 bytes long");
        }
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Extract claims from token
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new JwtException("Failed to parse JWT token: " + e.getMessage());
        }
    }

    // Generic method to extract any claim
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Check if the token has expired
    public boolean isTokenExpired(String token) {
        Date expirationDate = extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    // Validate token
    public boolean validateToken(String token, User user) {
        final String phoneNumber = extractClaim(token, Claims::getSubject);
        return (phoneNumber.equals(user.getPhoneNumber()) && !isTokenExpired(token));
    }

    // Extract phone number from token
    public String extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Validate token using UserDetails
    public boolean validateToken(String token, UserDetails userDetails) {
        String phoneNumber = extractPhoneNumber(token);
        return (phoneNumber.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
