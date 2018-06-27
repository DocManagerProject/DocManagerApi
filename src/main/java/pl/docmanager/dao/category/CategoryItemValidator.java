package pl.docmanager.dao.category;

import org.springframework.stereotype.Service;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.category.CategoryItemContentType;
import pl.docmanager.domain.page.Page;

import static pl.docmanager.domain.category.CategoryItemContentType.CATEGORY;
import static pl.docmanager.domain.category.CategoryItemContentType.PAGE;

@Service
public class CategoryItemValidator {

    public void validateCategoryItem(CategoryItem categoryItem) {
        if (categoryItem.getCategory() == null) {
            throw new EntityValidationException("CategoryItem's category cannot be null");
        }

        if (categoryItem.getIndex() < 0) {
            throw new EntityValidationException("CategoryItem's index cannot be negative");
        }

        CategoryItemContentType contentType = categoryItem.getContentType();
        Page contentPage = categoryItem.getContentPage();
        Category contentCategory = categoryItem.getContentCategory();

        if (contentType == null) {
            throw new EntityValidationException("CategoryItem's contentType cannot be null");
        }

        if (contentType == PAGE && (contentPage == null || contentCategory != null)) {
            throw new EntityValidationException("Wrong content of CategoryItem (type = PAGE, contentPage = " +
                    contentPage + ", contentCategory = " + contentCategory + ")");
        }

        if (contentType == CATEGORY && (contentCategory == null || contentPage != null)) {
            throw new EntityValidationException("Wrong content of CategoryItem (type = CATEGORY, contentPage = " +
                    contentPage + ", contentCategory = " + contentCategory + ")");
        }
    }
}
