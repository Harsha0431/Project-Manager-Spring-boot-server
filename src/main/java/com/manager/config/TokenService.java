package com.manager.config;

import com.model.User;
import com.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;


@Service
public class TokenService {

    @Value("${security.jwt.secret-key}")
    private String JWT_SECRET;
    @Value("${security.jwt.expiration-time}")
    private long JWT_EXPIRATION;

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Caught exception in isTokenValid in TokenService: " + e.getMessage());
            return false;
        }
    }

    public String getUserEmailFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String generateJwtToken(String email) {
        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + JWT_EXPIRATION))
            .signWith(key)
            .compact();
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7).strip();
        }
        return null;
    }

    public User getUserObjectFromHeaderToken(HttpServletRequest request, UserService userService){
        String email = getUserEmailFromToken(getTokenFromRequest(request));
        return userService.getUserObjectFromEmail(email);
    }
}
