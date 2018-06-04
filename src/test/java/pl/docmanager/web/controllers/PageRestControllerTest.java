package pl.docmanager.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.docmanager.dao.PageRepository;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageState;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PageRestController.class)
public class PageRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PageRepository pageRepository;

    @Before
    public void setup() {
        super.setup();

        Solution solution = new Solution();
        solution.setId(1);

        User author = new User();
        author.setId(99);

        Page page = new Page();
        page.setId(1);
        page.setAuthor(author);
        page.setCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0));
        page.setName("examplePage");
        page.setSections(new ArrayList<>());
        page.setState(PageState.ACTIVE);
        page.setUrl("example_page");
        page.setSolution(solution);

        given(pageRepository.findById(1L)).willReturn(Optional.of(page));

        Solution solution2 = new Solution();
        solution2.setId(2);

        User author2 = new User();
        author2.setId(199);

        Page page2 = new Page();
        page2.setId(2);
        page2.setAuthor(author2);
        page2.setCreateDate(LocalDateTime.now());
        page2.setName("examplePage");
        page2.setSections(new ArrayList<>());
        page2.setState(PageState.ACTIVE);
        page2.setUrl("example_page");
        page2.setSolution(solution2);

        given(pageRepository.findById(2L)).willReturn(Optional.of(page2));
    }

    @Test
    public void getPageByIdTestValid() throws Exception {
        String expectedJson = "{id: 1, solution: {id: 1}, name: 'examplePage', " +
                "createDate: '1970-01-01T00:00:00', " +
                "author: {'id': 99}, sections: [], state: 'ACTIVE', url: 'example_page'}";
        mvc.perform(get("/api/pages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getPageByIdTestNoApiToken() throws Exception {
        mvc.perform(get("/api/pages/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getPageByIdTestWrongApiToken() throws Exception {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        mvc.perform(get("/api/pages/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", invalidToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getPageByIdTestSettingsNotFound() throws Exception {
        mvc.perform(get("/api/pages/50")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getPageByIdTestNoAccessToSolution() throws Exception {
        mvc.perform(get("/api/pages/2")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}