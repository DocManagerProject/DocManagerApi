package pl.docmanager.web.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.UserRepository;
import pl.docmanager.domain.user.UserState;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static pl.docmanager.web.UserProvider.getMockUser;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceImplTest {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @MockBean
    private UserRepository userRepository;

    private static final String USER_EMAIL = "user@example.com";
    private static final String USER_PASSWORD = "examplePassword";

    @Before
    public void setup() {
        given(userRepository.findByEmail(USER_EMAIL))
                .willReturn(Optional.of(getMockUser(1, 1, USER_EMAIL, UserState.ACTIVE, USER_PASSWORD)));
    }

    @Test
    public void loadUserByUsernameTestValid() {
        EnchancedUserDetails userDetails = (EnchancedUserDetails) userDetailsService.loadUserByUsername(USER_EMAIL);
        assertEquals(USER_EMAIL, userDetails.getUsername());
        assertEquals(USER_PASSWORD, userDetails.getPassword());
        assertEquals(1, userDetails.getSolutionId());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameTestNullEmail() {
        userDetailsService.loadUserByUsername(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameTestNonExistingUser() {
        userDetailsService.loadUserByUsername("NonExistingUser");
    }
}