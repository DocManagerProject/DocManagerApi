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
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.validation.CategoryValidator;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

        Solution solution = new Solution();
        solution.setId(1);

        User author = new User();
        author.setId(99);

        Category category = new Category();
        category.setId(1);
        category.setAuthor(author);
        category.setCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0));
        category.setName("exampleCategory");
        category.setUrl("example_category");
        category.setState(CategoryState.ACTIVE);
        category.setSolution(solution);

        given(categoryRepository.findBySolution_IdAndUrl(1, "example_category")).willReturn(Optional.of(category));

        Solution solution2 = new Solution();
        solution2.setId(2);

        User author2 = new User();
        author2.setId(99);

        Category category2 = new Category();
        category2.setId(2);
        category2.setAuthor(author2);
        category2.setCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0));
        category2.setName("exampleCategory");
        category2.setUrl("example_category");
        category2.setState(CategoryState.ACTIVE);
        category2.setSolution(solution2);

        given(categoryRepository.findBySolution_IdAndUrl(2, "example_category")).willReturn(Optional.of(category2));
    }

    @Test
    public void getCategoryByIdAndUrlTestValid() throws Exception {
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
    public void getCategoryByIdAndUrlTestNoApiToken() throws Exception {
        mvc.perform(get("/api/categories/solution/1/url/example_category")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getCategoryByIdAndUrlTestWrongApiToken() throws Exception {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        mvc.perform(get("/api/categories/solution/1/url/example_category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", invalidToken))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void getCategoryByIdAndUrlTestCategoryNotFound() throws Exception {
        mvc.perform(get("/api/categories/solution/1/url/i_dont_exist")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getCategoryByIdAndUrlTestNoAccessToSolution() throws Exception {
        mvc.perform(get("/api/categories/solution/2/url/example_category")
                .contentType(MediaType.APPLICATION_JSON)
                .header("apiToken", validToken))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}