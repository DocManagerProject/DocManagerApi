package pl.docmanager.web.controllers.validation;

import org.springframework.stereotype.Service;
import pl.docmanager.domain.page.Page;
import pl.docmanager.web.controllers.exception.EntityValidationException;

@Service
public class PageValidator {
    public void validatePage(Page page) {
        // TODO: add more validation
        if (page.getSolution() == null) {
            throw new EntityValidationException("Page Solution cannot be null");
        }
    }
}
