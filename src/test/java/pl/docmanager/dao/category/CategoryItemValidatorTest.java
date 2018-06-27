package pl.docmanager.dao.category;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryBuilder;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.category.CategoryItemBuilder;
import pl.docmanager.domain.category.CategoryItemContentType;
import pl.docmanager.domain.page.PageBuilder;
import pl.docmanager.domain.solution.Solution;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoryItemValidatorTest {

    @Autowired
    private CategoryItemValidator categoryItemValidator;

    @Test
    public void validateCategoryItemTestValid() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(CategoryItemContentType.PAGE)
                .withContentPage(new PageBuilder(1, new Solution()).build())
                .withContentCategory(null)
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestNullCategory() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, null)
                .withContentType(CategoryItemContentType.PAGE)
                .withContentPage(new PageBuilder(1, new Solution()).build())
                .withContentCategory(null)
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestNullContentType() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(null)
                .withContentPage(new PageBuilder(1, new Solution()).build())
                .withContentCategory(null)
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestNullContentPage() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(CategoryItemContentType.PAGE)
                .withContentPage(null)
                .withContentCategory(null)
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestNullContentCategory() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(CategoryItemContentType.CATEGORY)
                .withContentPage(null)
                .withContentCategory(null)
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestTwoContents() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(CategoryItemContentType.PAGE)
                .withContentPage(new PageBuilder(1, new Solution()).build())
                .withContentCategory(new CategoryBuilder(1, new Solution()).build())
                .withIndex(0)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }

    @Test(expected = EntityValidationException.class)
    public void validateCategoryItemTestNegativeIndex() {
        CategoryItem categoryItem = new CategoryItemBuilder(1, new Category())
                .withContentType(CategoryItemContentType.PAGE)
                .withContentPage(new PageBuilder(1, new Solution()).build())
                .withContentCategory(null)
                .withIndex(-1)
                .build();
        categoryItemValidator.validateCategoryItem(categoryItem);
    }
}