package pl.docmanager.web.service.settings;

import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.settings.SettingsDao;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;
import pl.docmanager.web.service.ServiceTestBase;

import java.util.Date;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingsServiceTest extends ServiceTestBase {

    @Autowired
    private SettingsService settingsService;

    @MockBean
    private SettingsDao settingsDao;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void getSettingsByNameTestValid() {
        settingsService.getSettingsByName("test", 1, validToken);
        verify(settingsDao, times(1)).getSettingsByName("test", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSettingsByNameTestNullApiToken() {
        settingsService.getSettingsByName("test", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSettingsByNameTestEmptyApiToken() {
        settingsService.getSettingsByName("test", 1, null);
    }

    @Test(expected = SignatureException.class)
    public void getSettingsByNameTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        settingsService.getSettingsByName("test", 1, invalidToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getSettingsByNameTestNoAccessToSolution() {
        settingsService.getSettingsByName("test", 2, validToken);
    }

}