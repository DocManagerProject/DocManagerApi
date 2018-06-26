package pl.docmanager.dao.category;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.CategoryBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryDaoTest {

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
        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        category1 = new CategoryBuilder(1, solution)
                .withAuthor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(1, "example_category")).willReturn(Optional.of(category1));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category1));

        Solution solution2 = new SolutionBuilder(2).build();
        User author2 = new UserBuilder(199, solution2).build();
        category2 = new CategoryBuilder(2, solution2)
                .withAuthor(author2)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("exampleCategory")
                .withUrl("example_category").build();

        given(categoryRepository.findBySolution_IdAndUrl(2, "example_category")).willReturn(Optional.of(category2));
        given(categoryRepository.findById(2L)).willReturn(Optional.of(category2));
    }

    @Test
    public void getCategoryByIdTestValid() {
        assertEquals(category1, categoryDao.getCategoryById(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void getCategoryByIdTestNonExistingCategory() {
        categoryDao.getCategoryById(100);
    }

    @Test
    public void getCategoryByUrlTestValid() {
        assertEquals(category1, categoryDao.getCategoryByUrl("example_category", 1));
    }

    @Test(expected = NoSuchElementException.class)
    public void getCategoryByUrlTestNonExistingCategory() {
        categoryDao.getCategoryByUrl("i_dont_exist", 1);
    }

    @Test
    public void addCategoryTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, solution)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test(expected = EntityValidationException.class)
    public void addCategoryTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        Category category = new CategoryBuilder(0, null)
                .withName("category")
                .withUrl("url")
                .withAuthor(new UserBuilder(1, solution).build()).build();
        categoryDao.addCategory(category);
    }

    @Test
    public void updateCategoryNameTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        updatesMap.put("url", "newUrl");
        categoryDao.updateCategory(updatesMap, "example_category", 1);
        verify(categoryRepository, times(1)).save(any());
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryNameNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", null);
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryNameEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "");
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", null);
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", "");
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlTestUpdateId() {
        Map<String, Object> updatesMap = Maps.newHashMap("id", 5);
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlTestUpdateCreateDate() {
        Map<String, Object> updatesMap = Maps.newHashMap("createDate", LocalDateTime.now());
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlTestUpdateSolution() {
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new SolutionBuilder(5).build());
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updateCategoryUrlTestUpdateAuthor() {
        Solution solution = new SolutionBuilder(1).build();
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new UserBuilder(5, solution).build());
        categoryDao.updateCategory(updatesMap, "example_category", 1);
    }
}