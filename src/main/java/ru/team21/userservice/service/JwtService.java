package ru.team21.userservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import ru.team21.userservice.model.MyUser;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService extends BaseService {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        logger.debug("Extracting username from token");
        return extractClaim(token, Claims::getSubject);
    }

    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(extractAllClaims(token));
        } catch (JwtException e) {
            return false;
        }
    }

    public Long extractUserId(String token) {
        logger.debug("Extracting user id from token");
        return extractAllClaims(token).get("id", Long.class);
    }

    private Claims extractAllClaims(String token) {

        Claims claims = null;

        try {
            claims = Jwts
                    .parser()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            logger.debug("Claims successfully parsed from token");
        } catch (JwtException e) {
            logger.error("Error extracting claims from token {}", e.getMessage());
            throw new JwtException(e.getMessage());
        }
        return claims;

    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        logger.debug("Extracting specific claims from token");
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        logger.debug("Generating token from user {}", userDetails.getUsername());
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 36))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateToken(MyUser userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userDetails.getId());
        return generateToken(claims, userDetails);
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        logger.debug("Validating JWT token for user {}", userDetails.getUsername());
        String username = this.extractUsername(token);
        boolean isValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        if(!isValid) {
            logger.error("Invalid JWT token for user {}", userDetails.getUsername());
            throw new JwtException("Invalid JWT token");
        }
        else {
            logger.debug("Valid JWT token for user {}", userDetails.getUsername());
        }
        return isValid;
    }

    public boolean isTokenExpired(String token) {
        logger.debug("Checking if JWT token is expired");
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenExpired(Claims claims) {
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    private Date extractExpiration(String token) {
        logger.debug("Extracting expiration time from token");
        return extractClaim(token, Claims::getExpiration);
    }

    private Key getSignInKey() {
        logger.debug("Getting sign in key");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
