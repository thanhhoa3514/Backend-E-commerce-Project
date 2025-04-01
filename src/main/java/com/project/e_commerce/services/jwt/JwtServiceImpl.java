package com.project.e_commerce.services.jwt;

import com.project.e_commerce.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements IJwtService {

    @Value("${jwt.secretKey}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;

    @Override
    public String generateAccessToken(User user) {
        return buildToken(user, accessTokenExpirationMs);
    }

    @Override
    public String generateRefreshToken(User user) {
        return buildToken(user, refreshTokenExpirationMs);
    }

    private String buildToken(User user, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole().getName());
//        claims.put("jti", UUID.randomUUID().toString());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getPhoneNumber())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            log.error("Token is null or empty");
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {} - Token: {}", e.getMessage(), token);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {} - Token: {}", e.getMessage(), token);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {} - Token: {}", e.getMessage(), token);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {} - Token: {}", e.getMessage(), token);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {} - Token: {}", e.getMessage(), token);
        }
        return false;
    }

    @Override
    public String getUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String getRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}