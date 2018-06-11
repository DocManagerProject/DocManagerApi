package pl.docmanager.dao.category;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;
import pl.docmanager.dao.exception.EntityValidationException;

@Service
public class CategoryValidator {
    public void validateCategory(Category category) {
        // TODO: add more validation
        if (category.getSolution() == null) {
            throw new EntityValidationException("Category Solution cannot be null");
        }
    }
}
