package pl.docmanager.web.security;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.UserRepository;
import pl.docmanager.domain.user.User;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ApiTokenDecoder {

    private UserRepository userRepository;

    @Autowired
    public ApiTokenDecoder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUseFromApiToken(String apiToken) {

        if (apiToken == null) {
            throw new IllegalArgumentException("apiToken cannot be null");
        }

        String email = Jwts.parser()
                .setSigningKey(SecretKeeper.getInstance().getSecret().getBytes())
                .parseClaimsJws(apiToken)
                .getBody()
                .getSubject();
        Optional<User> optUser = userRepository.findByEmail(email);

        if (!optUser.isPresent()) {
            throw new NoSuchElementException("User with email: " + email + " not found");
        }

        return optUser.get();
    }
}
