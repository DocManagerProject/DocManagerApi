package pl.docmanager.dao.settings;

import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.DaoTestBase;
import pl.docmanager.domain.SettingsBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingsDaoTest extends DaoTestBase {

    @Autowired
    private SettingsDao settingsDao;

    @MockBean
    private SettingsRepository settingsRepository;

    private Settings settings1;
    private Settings settings2;

    @Before
    public void setup() {
        super.setup();

        Solution solution1 = new SolutionBuilder(1).build();
        settings1 = new SettingsBuilder(1, solution1)
                .withName("exampleName")
                .withValue("exampleValue")
                .withDomain("exampleDomain").build();

        given(settingsRepository.findBySolution_IdAndName(1, "test"))
                .willReturn(Optional.of(settings1));

        Solution solution2 = new SolutionBuilder(2).build();
        settings2 = new SettingsBuilder(2, solution2)
                .withName("exampleName")
                .withValue("exampleValue")
                .withDomain("exampleDomain").build();

        given(settingsRepository.findBySolution_IdAndName(2, "test"))
                .willReturn(Optional.of(settings2));
    }

    @Test
    public void getSettingsByNameTestValid() {
        assertEquals(settings1, settingsDao.getSettingsByName("test", 1, validToken));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSettingsByNameTestNullApiToken() {
        settingsDao.getSettingsByName("test", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getSettingsByNameTestEmptyApiToken() {
        settingsDao.getSettingsByName("test", 1, null);
    }

    @Test(expected = SignatureException.class)
    public void getSettingsByNameTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        settingsDao.getSettingsByName("test", 1, invalidToken);
    }

    @Test(expected = NoSuchElementException.class)
    public void getSettingsByNameTestNonExistingSettings() {
        settingsDao.getSettingsByName("i_dont_exist", 1, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getSettingsByNameTestNoAccessToSolution() {
        settingsDao.getSettingsByName("test", 2, validToken);
    }
}