package pl.docmanager.web.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class EnchancedUserDetails extends User {
    private long solutionId;

    public EnchancedUserDetails(String username, String password, long solutionId, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.solutionId = solutionId;
    }

    public long getSolutionId() {
        return solutionId;
    }
}
