package pl.docmanager.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.docmanager.dao.page.PageDao;
import pl.docmanager.domain.PageBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PageRestController.class)
public class PageRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PageDao pageDao;

    @Before
    public void setup() {
        super.setup();

        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        Page page = new PageBuilder(1, solution)
                .withAutor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("examplePage")
                .withUrl("example_page").build();

        given(pageDao.getPageByUrl("example_page", 1, validToken)).willReturn(page);
    }

    @Test
    public void getPageBySolutionIdAndUrlTestValid() throws Exception {
        String expectedJson = "{id: 1, solution: {id: 1}, name: 'examplePage', " +
                "createDate: '1970-01-01T00:00:00', " +
                "author: {'id': 99}, sections: [], state: 'ACTIVE', url: 'example_page'}";
        mvc.perform(get("/api/pages/solution/1/url/example_page")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void addPageTestValid() throws Exception {
        mvc.perform(post("/api/pages")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        "   \"name\": \"page\", " +
                        "   \"solution\": { " +
                        "      \"id\": 1 " +
                        "   }, " +
                        "   \"author\": { " +
                        "       \"id\": 1" +
                        "   }," +
                        "   \"url\": \"url\"," +
                        "   \"sections\": [{ " +
                        "       \"name\": \"section\", " +
                        "       \"content\": \"sectionContent\", " +
                        "       \"index\": 0, " +
                        "       \"url\": \"sectionUrl\" " +
                        "   }]"+
                        " }")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("apiToken", validToken))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
        verify(pageDao, times(1)).addPage(any(), eq(validToken));
    }

    @Test
    public void updatePageTestValid() throws Exception {
        mvc.perform(patch("/api/pages/solution/1/url/example_page")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ " +
                    "   \"name\": \"changedPageName\", " +
                    "   \"url\": \"changedUrl\" " +
                    " }")
            .accept(MediaType.APPLICATION_JSON)
            .characterEncoding("UTF-8")
            .header("apiToken", validToken))
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()));
        verify(pageDao, times(1))
                .updatePage(any(), any(), eq(1L), eq(validToken));
    }
}