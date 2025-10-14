package com.api.formSync.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${SECRET_KEY}")
    private String secretKey;

    @Value("${ENVIRONMENT}")
    private String environment;

    public String generateToken(Long id, Map<String, Object> claims, long expiry) {
        String token = Jwts.builder()
                .claims(claims)
                .subject(id.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiry * 1000))
                .signWith(getKey()).compact();
        
        if (environment.equals("local")) {
            System.out.println("Token Generated: " + token);
        }

        return token;
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Claims extractClaims(String token) {
        return extractAllClaims(token);
    }

    public boolean validateToken(String token, UserDetails details) {
        final String userId = extractEmail(token);
        return (userId.equals(details.getUsername()) && !isTokenExpired(token));
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    private SecretKey getKey() {
        byte[] bytes = Base64.getEncoder().withoutPadding().encode(secretKey.getBytes());
        return Keys.hmacShaKeyFor(bytes);
    }
}