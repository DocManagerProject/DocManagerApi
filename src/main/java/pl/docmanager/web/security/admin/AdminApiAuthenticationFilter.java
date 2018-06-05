package pl.docmanager.web.security.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.docmanager.web.security.ApiAuthenticationFilter;
import pl.docmanager.web.security.EnchancedUserDetails;
import pl.docmanager.web.security.JwtTokenGenerator;
import pl.docmanager.web.security.SecretKeeper;
import pl.docmanager.web.security.UserPasswordCredentials;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class AdminApiAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger log = LogManager.getLogger(ApiAuthenticationFilter.class);
    private AuthenticationManager authenticationManager;

    public AdminApiAuthenticationFilter(AuthenticationManager authenticationManager) {
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
            log.error("Error while authenticating admin user", e);
        }
        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                         FilterChain chain, Authentication authResult) {
        EnchancedUserDetails userDetails = (EnchancedUserDetails) authResult.getPrincipal();

        String apiToken = JwtTokenGenerator.generateToken(userDetails.getUsername(),
                SecretKeeper.getInstance().getAdminSecret(),
                new Date(System.currentTimeMillis() + 2_678_400_000L)); //expire after 1 month
        response.addHeader("adminApiToken", apiToken);
    }
}
