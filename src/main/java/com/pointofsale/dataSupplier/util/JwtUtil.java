package com.pointofsale.dataSupplier.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
    
    @Value("${pos.app.jwt.secret}")
    private String jwtSecret;

    @Value("${pos.app.jwt.expirationMs}")
    private Long jwtExpirationMs;

    // TODO get key from jwt secret
    private Key getStringKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // TODO extract claims from jwt token
    public <T> T extractClaim(Function<Claims, T> claimsResolver, String token) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // TODO extract username from jwt token
    public String extractUsernameFromJwtToken(String token) {
        return extractClaim(Claims::getSubject, token);
    }

    // TODO generate jwt token
    public String generateToken(Map<String, Object> extractClaims, String username) {
        return buildToken(extractClaims, username, jwtExpirationMs);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getStringKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    // TODO get expiration time
    public Long getExpiration() {
        return jwtExpirationMs;
    }

    // TODO extract all claims from jwt token
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getStringKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // TODO build token
    private String buildToken(Map<String, Object> extractClaims, String username, Long expiration) {
        return Jwts.builder()
                .setClaims(extractClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getStringKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
