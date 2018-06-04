package pl.docmanager.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenGenerator {

    public static String generateToken(String username, String secret, Date expiration) {
        if (secret == null) {
            throw new IllegalArgumentException("Secret cannot be null");
        }

        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }

        if (expiration == null) {
            throw new IllegalArgumentException("Expiration cannot be null");
        }

       return Jwts.builder()
               .setSubject(username)
               .setExpiration(expiration)
               .signWith(SignatureAlgorithm.HS512, secret.getBytes())
               .compact();
    }
}
