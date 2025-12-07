/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT Token provider for authentication.
 * Handles token generation, validation and parsing.
 * 
 * @author tai.buivan
 * @version 1.0
 */
@Component
public class JwtTokenProvider {

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIM_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";
    private static final String TOKEN_TYPE_REFRESH = "refresh";

    @Value("${jwt.secret:katmall-secret-key-must-be-at-least-256-bits-long-for-hs256}")
    private String jwtSecret;

    @Value("${jwt.access-token-expiration:3600000}")
    private long accessTokenExpiration; // 1 hour default

    @Value("${jwt.refresh-token-expiration:604800000}")
    private long refreshTokenExpiration; // 7 days default

    @Getter
    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Generates an access token for the user
     * 
     * @param userId User ID
     * @param email  User email
     * @param role   User role
     * @return JWT access token
     */
    public String generateAccessToken(String userId, String email, String role) {
        return generateToken(userId, email, role, accessTokenExpiration, TOKEN_TYPE_ACCESS);
    }

    /**
     * Generates a refresh token for the user
     * 
     * @param userId User ID
     * @param email  User email
     * @return JWT refresh token
     */
    public String generateRefreshToken(String userId, String email) {
        return generateToken(userId, email, null, refreshTokenExpiration, TOKEN_TYPE_REFRESH);
    }

    private String generateToken(String userId, String email, String role,
            long expiration, String tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        var builder = Jwts.builder()
                .subject(userId)
                .claim(CLAIM_EMAIL, email)
                .claim(CLAIM_TYPE, tokenType)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(signingKey);

        if (role != null) {
            builder.claim(CLAIM_ROLE, role);
        }

        return builder.compact();
    }

    /**
     * Extracts user ID from token
     * 
     * @param token JWT token
     * @return User ID
     */
    public String getUserIdFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts email from token
     * 
     * @param token JWT token
     * @return Email
     */
    public String getEmailFromToken(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_EMAIL, String.class));
    }

    /**
     * Extracts role from token
     * 
     * @param token JWT token
     * @return Role
     */
    public String getRoleFromToken(String token) {
        return extractClaim(token, claims -> claims.get(CLAIM_ROLE, String.class));
    }

    /**
     * Extracts expiration from token
     * 
     * @param token JWT token
     * @return Expiration date
     */
    public Date getExpirationFromToken(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Validates the token
     * 
     * @param token JWT token
     * @return true if valid
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if token is expired
     * 
     * @param token JWT token
     * @return true if expired
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Gets access token expiration in seconds
     * 
     * @return Expiration in seconds
     */
    public long getAccessTokenExpirationSeconds() {
        return accessTokenExpiration / 1000;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
