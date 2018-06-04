package pl.docmanager.web.security;

import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.UserRepository;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.domain.user.UserState;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static pl.docmanager.web.UserProvider.getMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTokenDecoderTest {

    @Autowired
    private ApiTokenDecoder apiTokenDecoder;
    @MockBean
    private UserRepository userRepository;

    private static final String USER_EMAIL = "user@example.com";
    private static final String WRONG_USER_EMAIL = "WrongEmail@example.com";

    @Before
    public void setup() {
        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(getMockUser(1, 1, USER_EMAIL, UserState.ACTIVE)));
        given(userRepository.findByEmail(WRONG_USER_EMAIL))
                .willReturn(Optional.of(getMockUser(2, 2, WRONG_USER_EMAIL, UserState.ACTIVE)));
    }

    @Test
    public void getUseFromApiTokenTestValid() {
        String apiToken = JwtTokenGenerator.generateToken(USER_EMAIL, SecretKeeper.getInstance().getSecret(),
                new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new Solution();
        solution.setId(1);
        User user = new User();
        user.setId(1);
        user.setSolution(solution);
        user.setEmail(USER_EMAIL);
        user.setState(UserState.ACTIVE);

        usersEqual(user, apiTokenDecoder.getUseFromApiToken(apiToken));
    }

    @Test(expected = SignatureException.class)
    public void getUserFromApiTokenTestWrongSecret() {
        String apiToken = JwtTokenGenerator.generateToken(USER_EMAIL, "WRONG_SECRET",
                new Date(System.currentTimeMillis() + 1000000000));
        apiTokenDecoder.getUseFromApiToken(apiToken);
    }

    @Test(expected = NoSuchElementException.class)
    public void getUserFromApiTokenTestNonExistingUserEmail() {
        String apiToken = JwtTokenGenerator.generateToken("NonExisting@example.com",
                SecretKeeper.getInstance().getSecret(),
                new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new Solution();
        solution.setId(1);
        User user = new User();
        user.setId(1);
        user.setSolution(solution);
        user.setEmail(USER_EMAIL);
        user.setState(UserState.ACTIVE);

        usersEqual(user, apiTokenDecoder.getUseFromApiToken(apiToken));
    }

    @Test
    public void getUserFromApiTokenTestWrongUserEmail() {
        String apiToken = JwtTokenGenerator.generateToken(WRONG_USER_EMAIL,
                SecretKeeper.getInstance().getSecret(),
                new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new Solution();
        solution.setId(1);
        assertNotEquals(USER_EMAIL, apiTokenDecoder.getUseFromApiToken(apiToken).getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserFromApiTokenTestNullToken() {
        apiTokenDecoder.getUseFromApiToken(null);
    }

    private void usersEqual(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getEmail(), user2.getEmail());
        assertEquals(user1.getState(), user2.getState());

        if (user1.getSolution() == null || user2.getSolution() == null) {
            assertEquals(user1.getSolution(), user2.getSolution());
        } else {
            assertEquals(user1.getSolution().getId(), user2.getSolution().getId());
        }
    }
}