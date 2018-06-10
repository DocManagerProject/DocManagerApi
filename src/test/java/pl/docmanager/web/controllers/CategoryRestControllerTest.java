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
import pl.docmanager.dao.CategoryRepository;
import pl.docmanager.domain.CategoryBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.validation.CategoryValidator;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerTest extends RestControllerTestBase {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryValidator categoryValidator;

    @Before
    public void setup() {
        super.setup();

        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        Category category = new CategoryBuilder(1, solution)
                .withAuthor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(1, "example_category")).willReturn(Optional.of(category));

        Solution solution2 = new SolutionBuilder(2).build();
        User author2 = new UserBuilder(199, solution2).build();
        Category category2 = new CategoryBuilder(1, solution2)
                .withAuthor(author2)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(2, "example_category")).willReturn(Optional.of(category2));
    }

    @Test
    public void getCategoryBySolutionIdAndUrlTestValid() throws Exception {
        String expectedJson = "{id: 1, solution: {id: 1}, name: 'exampleCategory', " +
                "createDate: '1970-01-01T00:00:00', " +
                "author: {'id': 99}, state: 'ACTIVE', url: 'example_category'}";
        mvc.perform(get("/api/categories/solution/1/url/example_category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    public void getCategoryBySolutionIdAndUrlTestNoApiToken() throws Exception {
        mvc.perform(get("/api/categories/solution/1/url/example_category")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getCategoryBySolutionIdAndUrlTestWrongApiToken() throws Exception {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        mvc.perform(get("/api/categories/solution/1/url/example_category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", invalidToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getCategoryBySolutionIdAndUrlTestCategoryNotFound() throws Exception {
        mvc.perform(get("/api/categories/solution/1/url/i_dont_exist")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getCategoryBySolutionIdAndUrlTestNoAccessToSolution() throws Exception {
        mvc.perform(get("/api/categories/solution/2/url/example_category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void addCategoryTestValid() throws Exception {
        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        "   \"name\": \"category\", " +
                        "   \"solution\": { " +
                        "      \"id\": 1 " +
                        "   }, " +
                        "   \"author\": { " +
                        "       \"id\": 1" +
                        "   }," +
                        "   \"url\": \"url\"" +
                        " }")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("apiToken", validToken))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()));
    }

    @Test
    public void addCategoryTestNullSolution() throws Exception {
        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        "   \"name\": \"category\", " +
                        "   \"author\": { " +
                        "       \"id\": 1" +
                        "   }," +
                        "   \"url\": \"url\"" +
                        " }")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("apiToken", validToken))
                .andDo(print())
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void addCategoryTestNotMySolution() throws Exception {
        mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ " +
                        "   \"name\": \"category\", " +
                        "   \"solution\": { " +
                        "      \"id\": 2 " +
                        "   }, " +
                        "   \"author\": { " +
                        "       \"id\": 1" +
                        "   }," +
                        "   \"url\": \"url\"" +
                        " }")
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .header("apiToken", validToken))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}