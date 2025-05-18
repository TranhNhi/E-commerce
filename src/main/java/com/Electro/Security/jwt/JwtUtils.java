package com.Electro.Security.jwt;

import com.Electro.Service.Impl.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.logging.Logger;

@Component
public class JwtUtils {
    private static final Logger logger = Logger.getLogger(JwtUtils.class.getName());

    @Value("${projectjavasneaker.app.jwtSecret}")
    private String jwtSecret;

    @Value("${projectjavasneaker.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .claim("role", (userPrincipal.getRole()))
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
            logger.severe("Invalid JWT signature: {}");
        } catch (MalformedJwtException e) {
            logger.severe("Invalid JWT token: {}");
        } catch (ExpiredJwtException e) {
            logger.severe("JWT token is expired: {}");
        } catch (UnsupportedJwtException e) {
            logger.severe("JWT token is unsupported: {}");
        } catch (IllegalArgumentException e) {
            logger.severe("JWT claims string is empty: {}");
        }

        return false;
    }
}