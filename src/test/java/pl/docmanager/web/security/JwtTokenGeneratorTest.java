package pl.docmanager.web.security;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class JwtTokenGeneratorTest {

    @Test
    public void generateTokenTestValid() {
        String secret = "exampleSecret";
        String userEmail = "user@example.com";
        Date expirationDate = new Date(1000000000);

        String expectedToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiZXhwIjoxMDAwMDAwfQ.ZBZwgsATmsIwsoFesuG8FUKZWDp1ntgygmjmhbmf3utTW6WN7dChIiSdVp9zxDR4VBgJpMPIu6EpPfpdlsZfkg";
        assertEquals(expectedToken, JwtTokenGenerator.generateToken(userEmail, secret, expirationDate));
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateTokenTestNullSecret() {
        String userEmail = "user@example.com";
        Date expirationDate = new Date(1000000000);
        JwtTokenGenerator.generateToken(userEmail, null, expirationDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateTokenTestNullUsername() {
        String secret = "exampleSecret";
        Date expirationDate = new Date(1000000000);
        JwtTokenGenerator.generateToken(null, secret, expirationDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateTokenTestNullExpiration() {
        String secret = "exampleSecret";
        String userEmail = "user@example.com";
        JwtTokenGenerator.generateToken(userEmail, secret, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void generateTokenTestEmptySecret() {
        String secret = "";
        String userEmail = "user@example.com";
        Date expirationDate = new Date(1000000000);
        JwtTokenGenerator.generateToken(userEmail, secret, expirationDate);
    }
}