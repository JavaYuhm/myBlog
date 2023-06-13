package com.myblog.config;

import com.myblog.domain.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenProvider {
    private final JwtProperties jwtProferties;

    public String generateToken(User user, Duration expiredAt){
        Date now = new Date();
        return makenToken(new Date(now.getTime()+expiredAt.toMillis()), user);
    }

    private String makenToken(Date expiry, User user){
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProferties.getIssuer())
                .setExpiration(expiry)
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .signWith(SignatureAlgorithm.HS256, jwtProferties.getSecretKey())
                .compact();
    }

    public boolean vaildToken(String token) {
        return this.getTokenClaims(token) != null;
    }

    public Authentication getAuthentication(String token){
        Claims claims = getTokenClaims(token);
        Set<SimpleGrantedAuthority> authorites = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorites), token, authorites);

    }

    public Long getUserId(String token){
        Claims claims = getTokenClaims(token);
        return claims.get("id", Long.class);
    }

    public Claims getTokenClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtProferties.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }
        return null;
    }

/*
    public boolean vaildToken(String token){
        try {
            Jwts.parser()
                    .setSigningKey(jwtProferties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
*/

}
