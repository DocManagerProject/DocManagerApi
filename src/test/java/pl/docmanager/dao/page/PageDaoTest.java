package pl.docmanager.dao.page;

import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.category.CategoryItemDao;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageBuilder;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.solution.SolutionBuilder;
import pl.docmanager.domain.user.User;
import pl.docmanager.domain.user.UserBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
public class PageDaoTest {

    @Autowired
    private PageDao pageDao;

    @MockBean
    private PageRepository pageRepository;
    @MockBean
    private CategoryItemDao categoryItemDao;

    @SpyBean
    private PageValidator pageValidator;

    private Page page1;
    private Page page2;

    @Before
    public void setup() {
        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        page1 = new PageBuilder(1, solution)
                .withAutor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("examplePage")
                .withContent("exampleContent")
                .withUrl("example_page").build();

        given(pageRepository.findBySolution_IdAndUrl(1, "example_page")).willReturn(Optional.of(page1));

        Solution solution2 = new SolutionBuilder(2).build();
        User author2 = new UserBuilder(199, solution2).build();
        page2 = new PageBuilder(2, solution2)
                .withAutor(author2)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("examplePage")
                .withContent("exampleContent")
                .withUrl("example_page").build();

        given(pageRepository.findBySolution_IdAndUrl(2, "example_page")).willReturn(Optional.of(page2));
    }

    @Test
    public void getPageByUrlTestValid() {
        assertEquals(page1, pageDao.getPageByUrl("example_page", 1));
    }

    @Test(expected = NoSuchElementException.class)
    public void getPageByUrlTestNonExistingPage() {
        pageDao.getPageByUrl("i_dont_exist", 1);
    }
    @Test
    public void addPageTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageDao.addPage(page);
        verify(pageRepository, times(1)).save(page);
    }

    @Test(expected = EntityValidationException.class)
    public void addPageTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, null)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageDao.addPage(page);
    }

    @Test
    public void updatePageTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        updatesMap.put("url", "newUrl");
        updatesMap.put("content", "newContent");
        pageDao.updatePage(updatesMap, "example_page", 1);
        verify(pageRepository, times(1)).save(any());
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageNameNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", null);
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageNameEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "");
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", null);
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", "");
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageContentNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("content", null);
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test
    public void updatePageContentEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("content", "");
        pageDao.updatePage(updatesMap, "example_page", 1);
        verify(pageRepository, times(1)).save(any());
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateId() {
        Map<String, Object> updatesMap = Maps.newHashMap("id", 5);
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateCreateDate() {
        Map<String, Object> updatesMap = Maps.newHashMap("createDate", LocalDateTime.now());
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateSolution() {
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new SolutionBuilder(5).build());
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateAuthor() {
        Solution solution = new SolutionBuilder(1).build();
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new UserBuilder(5, solution).build());
        pageDao.updatePage(updatesMap, "example_page", 1);
    }

    @Test
    public void addPageToCategoriesTestValid() {
        Page page = new PageBuilder(1, new Solution()).build();
        List<Long> ids = Arrays.asList(2L, 4L, 1L, 5L);
        pageDao.addPageToCategories(page, ids);
        verify(categoryItemDao, times(1)).removeAll(any());
        verify(categoryItemDao, times(1)).addAll(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageToCategoriesTestNullPage() {
        List<Long> ids = Arrays.asList(2L, 4L, 1L, 5L);
        pageDao.addPageToCategories(null, ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageToCategoriesTestNullCategoriesIds() {
        Page page = new PageBuilder(1, new Solution()).build();
        pageDao.addPageToCategories(page, null);
    }
}