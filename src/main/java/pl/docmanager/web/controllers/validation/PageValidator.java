package pl.docmanager.web.controllers.validation;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.exception.EntityValidationException;

@Service
public class PageValidator {
    public void validatePage(Page page) {
        // TODO: add more validation
        if (page.getSolution() == null) {
            throw new EntityValidationException("Page Solution cannot be null");
        }
    }

    public void validateLegalUpdate(User user, Page oldPage, Page newPage) {
        // TODO: add sections validation
        if (newPage.getAuthor() != null && newPage.getAuthor().getId() != oldPage.getAuthor().getId()) {
            throw new EntityValidationException("Cannot change Page's author");
        }

        if (newPage.getSolution() != null && newPage.getSolution().getId() != oldPage.getSolution().getId()) {
            throw new EntityValidationException("Cannot change Page's solution");
        }

        if (!newPage.getCreateDate().equals(oldPage.getCreateDate())) {
            throw new EntityValidationException("Cannot change Page's createDate");
        }

        if (newPage.getId() != oldPage.getId()) {
            throw new EntityValidationException("Cannot change Page's id");
        }

        if (newPage.getState() != oldPage.getState()) {
            throw new EntityValidationException("Cannot change Page's state");
        }
    }
}
