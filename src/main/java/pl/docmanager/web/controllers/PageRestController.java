package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.PageRepository;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class PageRestController {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private PageRepository pageRepository;

    @Autowired
    public PageRestController(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                              PageRepository pageRepository) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.pageRepository = pageRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/pages/{id}")
    public Page getPageById(@PathVariable(name = "id") long id,
                            @RequestHeader("apiToken") String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        Optional<Page> optPage = pageRepository.findById(id);

        if (optPage.isPresent()) {
            Page page = optPage.get();
            accessValidator.validateSolution(user, page.getSolution().getId());
            return page;
        }
        throw new NoSuchElementException();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return404(NoSuchElementException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AccessValidationException.class)
    public String return401(AccessValidationException e) {
        return e.getMessage();
    }
}
