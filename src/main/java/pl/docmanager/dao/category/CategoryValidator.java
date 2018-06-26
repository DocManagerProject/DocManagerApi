package pl.docmanager.dao.category;

import org.springframework.stereotype.Service;
import pl.docmanager.dao.exception.EntityValidationException;
import pl.docmanager.domain.category.Category;

import java.util.Map;

@Service
public class CategoryValidator {
    public void validateCategory(Category category) {
        // TODO: add more validation
        if (category.getSolution() == null) {
            throw new EntityValidationException("Category Solution cannot be null");
        }
    }

    public void validateLegalUpdate(Map<String, Object> updatesMap) {
        if (updatesMap.containsKey("author")) {
            throw new EntityValidationException("Cannot change Category's author");
        }

        if (updatesMap.containsKey("solution")) {
            throw new EntityValidationException("Cannot change Category's solution");
        }

        if (updatesMap.containsKey("createDate")) {
            throw new EntityValidationException("Cannot change Category's createDate");
        }

        if (updatesMap.containsKey("id")) {
            throw new EntityValidationException("Cannot change Category's id");
        }

        if (updatesMap.containsKey("state")) {
            // TODO allow state change
            throw new EntityValidationException("Cannot change Category's state");
        }

        if (updatesMap.containsKey("name") &&
                (updatesMap.get("name") == null || updatesMap.get("name").toString().isEmpty())) {
            throw new EntityValidationException("Category's name cannot be null nor empty");
        }

        if (updatesMap.containsKey("url") &&
                (updatesMap.get("url") == null || updatesMap.get("url").toString().isEmpty())) {
            throw new EntityValidationException("Category's url cannot be null nor empty");
        }
    }
}
