package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.PageRepository;
import pl.docmanager.domain.page.Page;
import pl.docmanager.domain.page.PageState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.exception.EntityValidationException;
import pl.docmanager.web.controllers.validation.PageValidator;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class PageRestController {

    private AccessValidator accessValidator;
    private PageValidator pageValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private PageRepository pageRepository;

    @Autowired
    public PageRestController(AccessValidator accessValidator, PageValidator pageValidator,
                              ApiTokenDecoder apiTokenDecoder, PageRepository pageRepository) {
        this.accessValidator = accessValidator;
        this.pageValidator = pageValidator;
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

    @RequestMapping(method = RequestMethod.POST, path = "/api/pages")
    public void addPage(@RequestBody Page page, @RequestHeader("apiToken") String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        pageValidator.validatePage(page, user);
        accessValidator.validateSolution(user, page.getSolution().getId());
        page.setCreateDate(LocalDateTime.now());
        page.setState(PageState.ACTIVE);
        pageRepository.save(page);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityValidationException.class)
    public String return400(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoSuchElementException.class, AccessValidationException.class})
    public String return404(Exception e) {
        return e.getMessage();
    }
}
