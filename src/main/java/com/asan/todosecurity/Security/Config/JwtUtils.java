package com.asan.todosecurity.Security.Config;

import com.asan.todosecurity.User.Model.UserDetailsImpl;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Log4j
@Component
public class JwtUtils  {

    /**
     * application.properties üzerinden jwtSecret ve timeout değerleri alınır
     */
    @Value("${todosecurity.app.jwtSecret}")
    private String jwtSecret;

    @Value("${todosecurity.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Token üretilen method
     * @param authentication
     * @return
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e);
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e);
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e);
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e);
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e);
        }

        return false;
    }
}

