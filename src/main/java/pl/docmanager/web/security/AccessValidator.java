package pl.docmanager.web.security;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.user.User;

@Service
public class AccessValidator {

    public void validateSolution(User user, long solutionId) {
        if (user.getSolution().getId() != solutionId) {
            throw new AccessValidationException("User " + user.getEmail() + " has no access to solution " + solutionId);
        }
    }
}
