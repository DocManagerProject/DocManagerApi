package pl.docmanager.web.service.category;

import io.jsonwebtoken.SignatureException;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.category.CategoryDao;
import pl.docmanager.domain.category.CategoryBuilder;
import pl.docmanager.domain.solution.SolutionBuilder;
import pl.docmanager.domain.user.UserBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;
import pl.docmanager.web.service.ServiceTestBase;

import java.util.Date;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryServiceTest extends ServiceTestBase {

    @Autowired
    private CategoryService categoryService;

    @MockBean
    private CategoryDao categoryDao;

    @Before
    public void setup() {
        super.setup();
        Solution solution1 = new SolutionBuilder(1).build();
        given(categoryDao.getCategoryById(1)).willReturn(new CategoryBuilder(1, solution1).build());
    }

    @Test
    public void getCategoryByIdTestValid() {
        categoryService.getCategoryById(1, 1, validToken);
        verify(categoryDao, times(1)).getCategoryById(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByIdTestNullApiToken() {
        categoryService.getCategoryById( 1, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByIdTestEmptyApiToken() {
        categoryService.getCategoryById(1, 1, "");
    }

    @Test(expected = SignatureException.class)
    public void getCategoryByIdTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        categoryService.getCategoryById(1, 1, invalidToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getCategoryByIdTestNoAccessToSolution() {
        categoryService.getCategoryById(2, 2, validToken);
    }

    @Test
    public void getCategoryByUrlTestValid() {
        categoryService.getCategoryByUrl("example_category", 1, validToken);
        verify(categoryDao, times(1)).getCategoryByUrl("example_category", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByUrlTestNullApiToken() {
        categoryService.getCategoryByUrl("example_category", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByUrlTestEmptyApiToken() {
        categoryService.getCategoryByUrl("example_category", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void getCategoryByUrlTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        categoryService.getCategoryByUrl("example_category", 1, invalidToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getCategoryByUrlTestNoAccessToSolution() {
        categoryService.getCategoryByUrl("example_category", 2, validToken);
    }

    @Test
    public void addCategoryTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryService.addCategory(category, validToken);
        verify(categoryDao, times(1)).addCategory(category);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCategoryTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, null)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryService.addCategory(category, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void addCategoryTestNotMySolution() {
        Solution solution1 = new SolutionBuilder(1).build();
        Solution solution2 = new SolutionBuilder(2).build();
        Category category = new CategoryBuilder(0, solution2)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution1).build()).build();
        categoryService.addCategory(category, validToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCategoryTestNullApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryService.addCategory(category, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCategoryTestEmptyApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryService.addCategory(category, "");
    }

    @Test(expected = SignatureException.class)
    public void addCategoryTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryService.addCategory(category, invalidToken);
    }

    @Test
    public void updateCategoryTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        updatesMap.put("url", "newUrl");
        categoryService.updateCategory(updatesMap, "example_category", 1, validToken);
        verify(categoryDao, times(1)).updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCategoryTestNullApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        categoryService.updateCategory(updatesMap, "example_category", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateCategoryTestEmptyApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        categoryService.updateCategory(updatesMap, "example_category", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void updateCategoryTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        categoryService.updateCategory(updatesMap, "example_category", 1, invalidToken);
    }
}