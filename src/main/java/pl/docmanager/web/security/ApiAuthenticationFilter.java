package pl.docmanager.web.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class ApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LogManager.getLogger(ApiAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;

    public static final String TEMPORARY_SECRET = "SecretKeyToGenJWTs";

    public ApiAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserPasswordCredentials credentials = new ObjectMapper()
                    .readValue(request.getInputStream(), UserPasswordCredentials.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(), credentials.getPassword()));
        } catch (IOException e) {
            log.error("Error while authenticating user", e);
        }
        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, Authentication authResult) {
        EnchancedUserDetails userDetails = (EnchancedUserDetails) authResult.getPrincipal();

        String apiToken = Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + 1000000000))
                .signWith(SignatureAlgorithm.HS512, TEMPORARY_SECRET.getBytes())
                .compact();
        response.addHeader("apiToken", apiToken);
        response.addHeader("solutionId", userDetails.getSolutionId() + "");
    }
}
