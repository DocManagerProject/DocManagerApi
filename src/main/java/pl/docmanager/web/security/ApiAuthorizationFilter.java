package pl.docmanager.web.security;

import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class ApiAuthorizationFilter extends BasicAuthenticationFilter {

    public ApiAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String apiToken = request.getHeader("apiToken");

        if (apiToken == null || apiToken.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        String user = Jwts.parser()
                .setSigningKey(ApiAuthenticationFilter.TEMPORARY_SECRET.getBytes())
                .parseClaimsJws(apiToken)
                .getBody()
                .getSubject();

        Authentication auth = user == null ? null : new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }
}
