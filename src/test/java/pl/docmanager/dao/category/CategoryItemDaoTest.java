package pl.docmanager.dao.category;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.DaoTestBase;
import pl.docmanager.domain.CategoryBuilder;
import pl.docmanager.domain.CategoryItemBuilder;
import pl.docmanager.domain.SolutionBuilder;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.solution.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryItemDaoTest extends DaoTestBase {

    @Autowired
    private CategoryItemDao categoryItemDao;

    @MockBean
    private CategoryItemRepository categoryItemRepository;
    @MockBean
    private CategoryRepository categoryRepository;

    private CategoryItem categoryItem1;
    private CategoryItem categoryItem2;
    private CategoryItem categoryItem3;
    private CategoryItem categoryItem4;
    private CategoryItem categoryItem5;
    private CategoryItem categoryItem6;

    @Before
    public void setup() {
        super.setup();
        Solution solution1 = new SolutionBuilder(1).build();
        Category category1 = new CategoryBuilder(1, solution1).withName("exampleCategory").build();
        categoryItem1 = new CategoryItemBuilder(1, category1).build();
        categoryItem2 = new CategoryItemBuilder(2, category1).build();
        categoryItem3 = new CategoryItemBuilder(3, category1).build();

        given(categoryRepository.findById(1L)).willReturn(Optional.of(category1));
        given(categoryItemRepository.findAllByCategory_Id(1))
                .willReturn(Arrays.asList(categoryItem1, categoryItem2, categoryItem3));

        Solution solution2 = new SolutionBuilder(2).build();
        Category category2 = new CategoryBuilder(2, solution2).withName("exampleCategory").build();
        categoryItem4 = new CategoryItemBuilder(4, category2).build();
        categoryItem5 = new CategoryItemBuilder(5, category2).build();
        categoryItem6 = new CategoryItemBuilder(6, category2).build();

        given(categoryRepository.findById(2L)).willReturn(Optional.of(category2));
        given(categoryItemRepository.findAllByCategory_Id(2))
                .willReturn(Arrays.asList(categoryItem4, categoryItem5, categoryItem6));

        Category category3 = new CategoryBuilder(3, solution1).withName("anotherExampleCategory").build();

        given(categoryRepository.findById(3L)).willReturn(Optional.of(category3));
        given(categoryItemRepository.findAllByCategory_Id(3)).willReturn(new ArrayList<>());
    }

    @Test
    public void getAllByCategoryIdTestValid() {
        List<CategoryItem> categoryItems = Arrays.asList(categoryItem1, categoryItem2, categoryItem3);
        assertTrue(categoryItems.containsAll(categoryItemDao.getAllByCategoryId(1)));
    }

    @Test
    public void getAllByCategoryIdTestValidCategoryEmpty() {
        assertTrue(categoryItemDao.getAllByCategoryId(3).isEmpty());
    }

    public void getAllByCategoryIdTestNonExistingCategory() {
        assertTrue(categoryItemDao.getAllByCategoryId(5).isEmpty());
    }
}