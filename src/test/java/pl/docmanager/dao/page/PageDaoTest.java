package pl.docmanager.dao.page;

import io.jsonwebtoken.SignatureException;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.DaoTestBase;
import pl.docmanager.dao.PageSectionRepository;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.PageBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageSection;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageDaoTest extends DaoTestBase {

    @Autowired
    private PageDao pageDao;

    @MockBean
    private PageRepository pageRepository;

    @MockBean
    private PageSectionRepository pageSectionRepository;

    @SpyBean
    private PageValidator pageValidator;

    private Page page1;
    private Page page2;

    @Before
    public void setup() {
        super.setup();
        Solution solution = new SolutionBuilder(1).build();
        User author = new UserBuilder(99, solution).build();
        page1 = new PageBuilder(1, solution)
                .withAutor(author)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("examplePage")
                .withUrl("example_page").build();

        given(pageRepository.findBySolution_IdAndUrl(1, "example_page")).willReturn(Optional.of(page1));

        Solution solution2 = new SolutionBuilder(2).build();
        User author2 = new UserBuilder(199, solution2).build();
        page2 = new PageBuilder(2, solution2)
                .withAutor(author2)
                .withCreateDate(LocalDateTime.of(1970, 1, 1, 0, 0))
                .withName("examplePage")
                .withUrl("example_page").build();

        given(pageRepository.findBySolution_IdAndUrl(2, "example_page")).willReturn(Optional.of(page2));
    }

    @Test
    public void getPageByUrlTestValid() {
        assertEquals(page1, pageDao.getPageByUrl("example_page", 1, validToken));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPageByUrlTestNullApiToken() {
        pageDao.getPageByUrl("example_page", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPageByUrlTestEmptyApiToken() {
        pageDao.getPageByUrl("example_page", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void getPageByUrlTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        pageDao.getPageByUrl("example_page", 1, invalidToken);
    }

    @Test(expected = NoSuchElementException.class)
    public void getPageByUrlTestNonExistingPage() {
        pageDao.getPageByUrl("i_dont_exist", 1, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getPageByUrlTestNoAccessToSolution() {
        pageDao.getPageByUrl("example_page", 2, validToken);
    }

    @Test
    public void addPageTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, validToken);
        verify(pageRepository, times(1)).save(page);
        verify(pageSectionRepository, times(1)).saveAll(pageSections);
    }

    @Test(expected = EntityValidationException.class)
    public void addPageTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, null)
                .withName("page")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void addPageTestNotMySolution() {
        Solution solution1 = new SolutionBuilder(1).build();
        Solution solution2 = new SolutionBuilder(2).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, solution2)
                .withName("page")
                .withAutor(new UserBuilder(1, solution1).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, validToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageTestNullApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageTestEmptyApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, "");
    }

    @Test(expected = SignatureException.class)
    public void addPageTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new SolutionBuilder(1).build();
        List<PageSection> pageSections = Collections.singletonList(new PageSection());
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url")
                .withSections(pageSections).build();
        pageDao.addPage(page, invalidToken);
    }

    @Test
    public void updatePageNameTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        Page page = pageDao.updatePage(updatesMap, "example_page", 1, validToken);

        assertEquals("newName", page.getName());
        verify(pageRepository, times(1)).save(page);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageNameNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", null);
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageNameEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "");
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test
    public void updatePageUrlTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", "newUrl");
        Page page = pageDao.updatePage(updatesMap, "example_page", 1, validToken);

        assertEquals("newUrl", page.getUrl());
        verify(pageRepository, times(1)).save(page);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlNullTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", null);
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlEmptyTest() {
        Map<String, Object> updatesMap = Maps.newHashMap("url", "");
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePageTestNullApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageDao.updatePage(updatesMap, "example_page", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePageTestEmptyApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageDao.updatePage(updatesMap, "example_page", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void updatePageTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageDao.updatePage(updatesMap, "example_page", 1, invalidToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateId() {
        Map<String, Object> updatesMap = Maps.newHashMap("id", 5);
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateCreateDate() {
        Map<String, Object> updatesMap = Maps.newHashMap("createDate", LocalDateTime.now());
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateSolution() {
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new SolutionBuilder(5).build());
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }

    @Test(expected = EntityValidationException.class)
    public void updatePageUrlTestUpdateAuthor() {
        Solution solution = new SolutionBuilder(1).build();
        Map<String, Object> updatesMap = Maps.newHashMap("solution", new UserBuilder(5, solution).build());
        pageDao.updatePage(updatesMap, "example_page", 1, validToken);
    }
}