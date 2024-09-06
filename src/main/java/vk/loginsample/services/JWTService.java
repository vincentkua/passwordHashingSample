package vk.loginsample.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    // Secret key for signing the JWT (store securely in production)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Method to generate JWT token
    public String generateJwtToken(String username) {
        long expirationTime = 864_000_000; // 10 days in milliseconds

        // Claims for the JWT payload
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        // Create and return the JWT token
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key) // Sign the token with the secret key
                .compact();
    }

    // Method to validate JWT token
    public boolean validateJwtToken(String token, String username) {
        String tokenUsername = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        return (username.equals(tokenUsername) && !isTokenExpired(token));
    }

    // Helper method to check if the token is expired
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
