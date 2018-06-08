package pl.docmanager.web.controllers.validation;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.exception.EntityValidationException;

@Service
public class PageValidator {
    public void validatePage(Page page, User requester) {
        // TODO: add more validation
        if (page.getSolution() == null) {
            throw new EntityValidationException("Page Solution cannot be null");
        }

        if (page.getAuthor() == null) {
            throw new EntityValidationException("Page Author cannot be null");
        }

        if (page.getAuthor().getId() != requester.getId()) {
            throw new EntityValidationException("Page Author's id doesn't match requester id");
        }
    }
}
