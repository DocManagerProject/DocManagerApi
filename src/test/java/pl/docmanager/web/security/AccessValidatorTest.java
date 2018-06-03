package pl.docmanager.web.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccessValidatorTest {

    @Autowired
    private AccessValidator accessValidator;

    @Test
    public void validateSolutionTestValid() {
        Solution solution = new Solution();
        solution.setId(99);
        User user = new User();
        user.setId(1);
        user.setSolution(solution);
        accessValidator.validateSolution(user, 99);
    }

    @Test(expected = AccessValidationException.class)
    public void validateSolutionTestNoAccess() {
        Solution solution = new Solution();
        solution.setId(99);
        User user = new User();
        user.setId(1);
        user.setSolution(solution);
        accessValidator.validateSolution(user, 100);
    }
}