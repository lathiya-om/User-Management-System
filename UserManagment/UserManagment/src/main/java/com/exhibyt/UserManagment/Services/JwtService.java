package com.exhibyt.UserManagment.Services;

import com.exhibyt.UserManagment.Entity.RefreshToken;
import com.exhibyt.UserManagment.Entity.User;
import com.exhibyt.UserManagment.Repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);
    private final RefreshTokenRepository refreshTokenRepo;

    public JwtService(RefreshTokenRepository refreshTokenRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
    }

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMillis;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMillis;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            log.warn("Failed to extract username from token: {}", e.getMessage());
            throw e;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        try {
            log.debug("Extracting all claims from JWT");
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            throw e;
        }
    }

    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        Object rolesObj = claims.get("roles");

        if (rolesObj instanceof List<?>) {
            return ((List<?>) rolesObj).stream()
                    .filter(role -> role instanceof String)
                    .map(Object::toString)
                    .toList();
        }

        return List.of();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
            if (!isValid) {
                log.warn("Invalid or expired JWT token for user '{}'", userDetails.getUsername());
            } else {
                log.debug("JWT token is valid for user '{}'", username);
            }
            return isValid;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        if (expired) {
            log.debug("Token is expired");
        }
        return expired;
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.debug("Generating token with roles: {}", roles); // ✅ DEBUG LOG

        extraClaims.put("roles", roles);

        String token = generateToken(extraClaims, userDetails);
        log.info("Generated access token with roles for user '{}'", userDetails.getUsername());
        return token;
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        RefreshToken refreshToken = refreshTokenRepo.findByUser(user)
                .orElse(new RefreshToken());

        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationMillis));

        refreshTokenRepo.save(refreshToken);
        log.info("Generated and stored refresh token for user '{}'", user.getUsername());
        return refreshToken.getToken();
    }

    public boolean isRefreshTokenExpired(RefreshToken refreshToken) {
        boolean expired = refreshToken.getExpiryDate().isBefore(Instant.now());
        if (expired) {
            log.debug("Refresh token is expired for user '{}'", refreshToken.getUser().getUsername());
        }
        return expired;
    }
}
