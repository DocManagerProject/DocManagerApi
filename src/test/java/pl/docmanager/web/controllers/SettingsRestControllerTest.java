package pl.docmanager.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.docmanager.dao.SettingsRepository;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsRestController.class)
public class SettingsRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SettingsRepository settingsRepository;

    @Before
    public void setup() {
        super.setup();

        Solution solution1 = new Solution();
        solution1.setId(1);

        Settings settings = new Settings();
        settings.setId(1);
        settings.setSolution(solution1);
        settings.setDomain("exampleDomain");
        settings.setName("exampleName");
        settings.setValue("exampleValue");

        given(settingsRepository.findBySolution_IdAndName(1, "test"))
                .willReturn(Optional.of(settings));

        Solution solution2 = new Solution();
        solution2.setId(2);

        Settings settings2 = new Settings();
        settings2.setId(2);
        settings2.setSolution(solution2);
        settings2.setDomain("exampleDomain");
        settings2.setName("exampleName");
        settings2.setValue("exampleValue");

        given(settingsRepository.findBySolution_IdAndName(2, "test"))
                .willReturn(Optional.of(settings2));
    }

    @Test
    public void getSettingsBySolutionAndNameTestValid() throws Exception {
        String expectedJson = "{'id': 1, 'solution': {id: 1}, 'name': 'exampleName', 'value': 'exampleValue', " +
                "'domain': 'exampleDomain'}";
        mvc.perform(get("/api/settings/solution/1/name/test")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getSettingsBySolutionAndNameTestNoApiToken() throws Exception {
        mvc.perform(get("/api/settings/solution/1/name/test")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getSettingsBySolutionAndNameTestWrongApiToken() throws Exception {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret");
        mvc.perform(get("/api/settings/solution/1/name/test")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", invalidToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getSettingsBySolutionAndNameTestSettingsNotFound() throws Exception {
        mvc.perform(get("/api/settings/solution/1/name/wrongsettings")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getSettingsBySolutionAndNameTestNoAccessToSolution() throws Exception {
        mvc.perform(get("/api/settings/solution/2/name/test")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}