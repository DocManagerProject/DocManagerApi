package pl.docmanager.web.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtTokenGenerator {

    public static String generateToken(String username, String secret) {
       return Jwts.builder()
               .setSubject(username)
               .setExpiration(new Date(System.currentTimeMillis() + 1000000000))
               .signWith(SignatureAlgorithm.HS512, secret.getBytes())
               .compact();
    }
}
