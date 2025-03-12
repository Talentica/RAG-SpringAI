package com.openAi.utils;

import com.openAi.exceptions.InvalidAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${security.jwtSecret}")
    private String jwtSecret;

    @Value("${security.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getJWTToken(String username) {
        List<GrantedAuthority> grantedAuthorities =
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        String token =
                Jwts.builder()
                        .setId("softtekJWT")
                        .setSubject(username)
                        .claim(
                                "authorities",
                                grantedAuthorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .toList())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                        .signWith(key, SignatureAlgorithm.HS512)
                        .compact();
        logger.info("Token successfully generated for user: {}", username);
        return token;
    }

    public void validateJwtToken(String authToken) {
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).getBody();
        }
        catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw e;
        } catch (ExpiredJwtException e) {
            final String errorMessage = String.format("JWT token is expired | %s", e.getMessage());
            throw InvalidAuthenticationException.invalidToken(errorMessage);
        }
    }

    public String getSubject(String jwt) {
        return getAllClaimsFromToken(jwt).getSubject();
    }

    public Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Could not get all claims Token from passed token");
            claims = null;
        }
        return claims;
    }
}
