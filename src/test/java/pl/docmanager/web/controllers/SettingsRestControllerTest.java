package pl.docmanager.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.docmanager.dao.settings.SettingsDao;
import pl.docmanager.domain.SettingsBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.solution.Solution;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SettingsRestController.class)
public class SettingsRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SettingsDao settingsDao;

    @Before
    public void setup() {
        super.setup();

        Solution solution1 = new SolutionBuilder(1).build();
        Settings settings = new SettingsBuilder(1, solution1)
                .withName("exampleName")
                .withValue("exampleValue")
                .withDomain("exampleDomain").build();

        given(settingsDao.getSettingsByName("test", 1, validToken))
                .willReturn(settings);

        Solution solution2 = new SolutionBuilder(2).build();
        Settings settings2 = new SettingsBuilder(2, solution2)
                .withName("exampleName")
                .withValue("exampleValue")
                .withDomain("exampleDomain").build();

        given(settingsDao.getSettingsByName("test", 2, validToken))
                .willReturn(settings2);
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
}