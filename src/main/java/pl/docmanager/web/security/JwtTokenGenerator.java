package pl.docmanager.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenGenerator {

    public static String generateToken(String username, String secret, Date expiration) {
       return Jwts.builder()
               .setSubject(username)
               .setExpiration(expiration)
               .signWith(SignatureAlgorithm.HS512, secret.getBytes())
               .compact();
    }
}
