package pl.docmanager.dao.page;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.user.User;
import pl.docmanager.dao.exception.EntityValidationException;

import java.util.Map;

@Service
public class PageValidator {
    public void validatePage(Page page) {
        // TODO: add more validation
        if (page.getSolution() == null) {
            throw new EntityValidationException("Page Solution cannot be null");
        }
    }

    public void validateLegalUpdate(User user, Page page, Map<String, Object> updatesMap) {
        // TODO: add sections validation
        if (updatesMap.containsKey("author")) {
            throw new EntityValidationException("Cannot change Page's author");
        }

        if (updatesMap.containsKey("solution")) {
            throw new EntityValidationException("Cannot change Page's solution");
        }

        if (updatesMap.containsKey("createDate")) {
            throw new EntityValidationException("Cannot change Page's createDate");
        }

        if (updatesMap.containsKey("id")) {
            throw new EntityValidationException("Cannot change Page's id");
        }

        if (updatesMap.containsKey("state")) {
            // TODO allow state change
            throw new EntityValidationException("Cannot change Page's state");
        }

        if (updatesMap.containsKey("name") &&
                (updatesMap.get("name") == null || updatesMap.get("name").toString().isEmpty())) {
            throw new EntityValidationException("Page's name cannot be null nor empty");
        }

        if (updatesMap.containsKey("content") && updatesMap.get("content") == null) {
            throw new EntityValidationException("Page's content cannot be null");
        }

        if (updatesMap.containsKey("url") &&
                (updatesMap.get("url") == null || updatesMap.get("url").toString().isEmpty())) {
            throw new EntityValidationException("Page's url cannot be null nor empty");
        }
    }
}
