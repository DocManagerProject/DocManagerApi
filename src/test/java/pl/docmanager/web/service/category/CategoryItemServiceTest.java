package pl.docmanager.web.service.category;

import io.jsonwebtoken.SignatureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.category.CategoryDao;
import pl.docmanager.dao.category.CategoryItemDao;
import pl.docmanager.domain.category.CategoryBuilder;
import pl.docmanager.domain.solution.SolutionBuilder;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.JwtTokenGenerator;
import pl.docmanager.web.service.ServiceTestBase;

import java.util.Date;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryItemServiceTest extends ServiceTestBase {

    @Autowired
    private CategoryItemService categoryItemService;

    @MockBean
    private CategoryItemDao categoryItemDao;

    @MockBean
    private CategoryDao categoryDao;

    @Before
    public void setup() {
        super.setup();
        given(categoryDao.getCategoryById(1)).willReturn(new CategoryBuilder(1, new SolutionBuilder(1).build()).build());
        given(categoryDao.getCategoryById(2)).willReturn(new CategoryBuilder(2, new SolutionBuilder(2).build()).build());
    }

    @Test
    public void getAllByCategoryIdTestValid() {
        categoryItemService.getAllByCategoryId(1, validToken);
        verify(categoryItemDao, times(1)).getAllByCategoryId(1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllByCategoryIdTestNullApiToken() {
        categoryItemService.getAllByCategoryId(1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAllByCategoryIdTestEmptyApiToken() {
        categoryItemService.getAllByCategoryId(1, "");
    }

    @Test(expected = SignatureException.class)
    public void getAllByCategoryIdTestWrongApiToken() {
        String invalidToken = JwtTokenGenerator.generateToken(USER_EMAIL, "invalidSecret", new Date(System.currentTimeMillis() + 1000000000));
        categoryItemService.getAllByCategoryId(1, invalidToken);
    }

    @Test(expected = AccessValidationException.class)
    public void getAllByCategoryIdTestNoAccessToSolution() {
        categoryItemService.getAllByCategoryId(2, validToken);
    }
}