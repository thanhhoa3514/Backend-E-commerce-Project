package com.project.e_commerce.components;

import com.project.e_commerce.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    @Value("${jwt.expiration}")
    private  Long timeExpiration; // Need to save an environment variables

    @Value("${jwt.secretKey}")
    private String secretKey;


    public String generateToken(com.project.e_commerce.models.User user) {
        //  Convert User's properties to properties in spring security is called Claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole().getName());
        try {
            // Log để debug
            System.out.println("Generating token for user: " + user.getPhoneNumber());
            System.out.println("User role: " + user.getRole().getName());
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + timeExpiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Đổi sang HS256
                    .compact();
        }catch (Exception e){
            throw new RuntimeException("Could not generate token", e);
        }
    }
    private Key getSignInKey(){
//        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] bytes= Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(bytes);
    }
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims=this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token){
        return extractAllClaims(token).get("phoneNumber").toString();
    }
    public boolean isTokenExpired(String token){
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
    public boolean validateToken(String token, User user) {
        try {
            final String username = extractUsername(token);
            return (username.equals(user.getPhoneNumber())) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    
}
