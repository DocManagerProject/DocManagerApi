package pl.docmanager.web.security.admin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pl.docmanager.web.security.SecretKeeper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminApiAuthorizationFilter extends BasicAuthenticationFilter {

    public AdminApiAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String apiToken = request.getHeader("adminApiToken");

        if (apiToken == null || apiToken.isEmpty()) {
            chain.doFilter(request, response);
            return;
        }

        Authentication auth;
        try {
            String user = Jwts.parser()
                    .setSigningKey(SecretKeeper.getInstance().getAdminSecret().getBytes())
                    .parseClaimsJws(apiToken)
                    .getBody()
                    .getSubject();
            List<GrantedAuthority> authorityList = new ArrayList<>();
            authorityList.add(new SimpleGrantedAuthority("ROLE_admin"));
            auth = user == null ? null : new UsernamePasswordAuthenticationToken(user, null, authorityList);
        } catch (SignatureException e) {
            auth = null;
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }
}
