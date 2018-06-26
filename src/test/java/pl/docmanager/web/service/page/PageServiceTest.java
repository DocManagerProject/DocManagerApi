package pl.docmanager.web.service.page;

import io.jsonwebtoken.SignatureException;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.page.PageDao;
import pl.docmanager.domain.PageBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.UserBuilder;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.solution.Solution;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;
import pl.docmanager.web.service.ServiceTestBase;

import java.util.Date;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageServiceTest extends ServiceTestBase {

    @Autowired
    private PageService pageService;

    @MockBean
    private PageDao pageDao;

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void getPageByUrlTestValid() {
        pageService.getPageByUrl("example_page", 1, validToken);
        verify(pageDao, times(1)).getPageByUrl("example_page", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPageByUrlTestNullApiToken() {
        pageService.getPageByUrl("example_page", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getPageByUrlTestEmptyApiToken() {
        pageService.getPageByUrl("example_page", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void getPageByUrlTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        pageService.getPageByUrl("example_page", 1, invalidToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getPageByUrlTestNoAccessToSolution() {
        pageService.getPageByUrl("example_page", 2, validToken);
    }

    @Test
    public void addPageTestValid() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageService.addPage(page, validToken);
        verify(pageDao, times(1)).addPage(page);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageTestNullSolution() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, null)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageService.addPage(page, validToken);
    }

    @Test(expected = AccessValidationException.class)
    public void addPageTestNotMySolution() {
        Solution solution1 = new SolutionBuilder(1).build();
        Solution solution2 = new SolutionBuilder(2).build();
        Page page = new PageBuilder(0, solution2)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution1).build())
                .withUrl("url").build();
        pageService.addPage(page, validToken);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageTestNullApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageService.addPage(page, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addPageTestEmptyApiToken() {
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageService.addPage(page, "");
    }

    @Test(expected = SignatureException.class)
    public void addPageTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Solution solution = new SolutionBuilder(1).build();
        Page page = new PageBuilder(0, solution)
                .withName("page")
                .withContent("exampleContent")
                .withAutor(new UserBuilder(1, solution).build())
                .withUrl("url").build();
        pageService.addPage(page, invalidToken);
    }

    @Test
    public void updatePageTestValid() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        updatesMap.put("url", "newUrl");
        updatesMap.put("content", "newContent");
        pageService.updatePage(updatesMap, "example_page", 1, validToken);
        verify(pageDao, times(1)).updatePage(updatesMap, "example_page", 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePageTestNullApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageService.updatePage(updatesMap, "example_page", 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatePageTestEmptyApiToken() {
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageService.updatePage(updatesMap, "example_page", 1, "");
    }

    @Test(expected = SignatureException.class)
    public void updatePageTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        Map<String, Object> updatesMap = Maps.newHashMap("name", "newName");
        pageService.updatePage(updatesMap, "example_page", 1, invalidToken);
    }
}