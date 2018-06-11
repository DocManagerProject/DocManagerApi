package pl.docmanager.dao.category;

import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.DaoTestBase;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.CategoryBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDaoTest extends DaoTestBase {

    @Autowired
    private CategoryDao categoryDao;

    @MockBean
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryValidator categoryValidator;

    private Category category1;
    private Category category2;

    @Before
    public void setup() {
        super.setup();
        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        category1 = new CategoryBuilder(1, solution)
                .withAuthor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(1, "example_category")).willReturn(Optional.of(category1));

        Solution solution2 = new SolutionBuilder(2).build();
        User author2 = new UserBuilder(199, solution2).build();
        category2 = new CategoryBuilder(1, solution2)
                .withAuthor(author2)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(2, "example_category")).willReturn(Optional.of(category2));

    }

    @Test
    public void getCategoryByUrlTestValid() {
        assertEquals(category1, categoryDao.getCategoryByUrl("example_category", 1, validToken));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByUrlTestNullApiToken() {
        categoryDao.getCategoryByUrl("example_category", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCategoryByUrlTestEmptyApiToken() {
        categoryDao.getCategoryByUrl("example_category", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void getCategoryByUrlTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        categoryDao.getCategoryByUrl("example_category", 1, invalidToken);
    }

    @Test(expected = NoSuchElementException.class)
    public void getCategoryByUrlTestNonExistingCategory() {
        categoryDao.getCategoryByUrl("i_dont_exist", 1, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getCategoryByUrlTestNoAccessToSolution() {
        categoryDao.getCategoryByUrl("example_category", 2, validToken);
    }

    @Test
    public void addCategoryTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category, validToken);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test(expected = EntityValidationException.class)
    public void addCategoryTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, null)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void addCategoryTestNotMySolution() {
        Solution solution1 = new SolutionBuilder(1).build();
        Solution solution2 = new SolutionBuilder(2).build();
        Category category = new CategoryBuilder(0, solution2)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution1).build()).build();
        categoryDao.addCategory(category, validToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCategoryTestNullApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addCategoryTestEmptyApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category, "");
    }

    @Test(expected = SignatureException.class)
    public void addCategoryTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category, invalidToken);
    }
}