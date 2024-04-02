package com.example.springbootecommerce.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.springbootecommerce.model.MyUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwtSecret}")
    private String jwtSecret;

    @Value("${jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(MyUserDetails myUserDetails) {
        return JWT.create()
                .withClaim("id", myUserDetails.getUser().getId())
                .withClaim("username", myUserDetails.getUsername())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date((new Date()).getTime() + jwtExpiration))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return JWT.decode(token).getClaims().get("username").asString();
    }

    public boolean validateJwtToken(String authToken) {
        if (Boolean.FALSE.equals(isTokenExpired(authToken))) {
            try {
                JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(authToken);
                return true;
            } catch (JWTVerificationException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = JWT.decode(token).getExpiresAt();
        return expiration.before(new Date());
    }
}
