package pl.docmanager.web.controllers.validation;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;
import pl.docmanager.web.controllers.exception.EntityValidationException;

@Service
public class CategoryValidator {
    public void validateCategory(Category category) {
        // TODO: add more validation
        if (category.getSolution() == null) {
            throw new EntityValidationException("Category Solution cannot be null");
        }
    }
}
