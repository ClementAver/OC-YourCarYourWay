package com.openclassrooms.ycyw_back.services;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService implements JwtInterface {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-expiration-time}")
    private long jwtRefreshExpiration;

    /**
     * Extract the username from the token
     * @param token The token
     * @return The username
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract the claim from the token
     * @param token The token to extract the claim from
     * @param claimsResolver The function that extracts the claim from the Claims object
     * @param <T> The type of the claim
     * @return The claim
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate a token for a user
     * @param userDetails The user details to include in the token
     * @return The generated JWT
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate a refresh token for a user
     * @param userDetails The user details to include in the token
     * @return The generated JWT
     */
    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    /**
     * Generate a token for a user
     * @param extraClaims The extra claims to include in the token
     * @param userDetails The user details to include in the token
     * @return The generated JWT
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Generate a refresh token for a user
     * @param extraClaims The extra claims to include in the token
     * @param userDetails The user details to include in the token
     * @return The generated JWT
     */
    @Override
    public String generateRefreshToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtRefreshExpiration);
    }

    /**
     * Get the expiration time for an access token from the application's properties
     * @return The expiration time
     */
    @Override
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Get the expiration time of the refresh token from the application's properties
     * @return The expiration time
     */
    @Override
    public long getRefreshExpirationTime() {
        return jwtRefreshExpiration;
    }

    /**
     * Build a token
     * @param extraClaims The extra claims to include in the token
     * @param userDetails The user details to include in the token
     * @param expiration The expiration time of the token
     * @return The generated JWT
     */
    @Override
    public String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validate a token
     * @param token The token to validate
     * @param userDetails The user details to validate the token against
     * @return Whether the token is valid or not
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Validate a refresh token
     * @param token The token to validate
     * @return Whether the token is valid or not
     */
    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract the expiration date from a token
     * @param token The token
     * @return The expiration date of the token
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from the provided JWT token
     * @param token The JWT token to extract claims from
     * @return The claims contained in the token
     * @throws ExpiredJwtException If the token has expired
     * @throws MalformedJwtException If the token is malformed
     * @throws SignatureException If the token's signature is invalid
     */
    @Override
    public Claims extractAllClaims(String token) throws ExpiredJwtException, MalformedJwtException, SignatureException {
            return Jwts.parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
    }

    /**
     * Get the key used to sign the token
     * @return The signing key used for token generation and verification
     */
    @Override
    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}