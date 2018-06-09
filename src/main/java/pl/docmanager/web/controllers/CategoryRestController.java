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
import pl.docmanager.dao.CategoryRepository;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.exception.EntityValidationException;
import pl.docmanager.web.controllers.validation.CategoryValidator;
import pl.docmanager.web.security.AccessValidationException;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class CategoryRestController {

    private AccessValidator accessValidator;
    private CategoryValidator categoryValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(AccessValidator accessValidator, CategoryValidator categoryValidator,
                                  ApiTokenDecoder apiTokenDecoder, CategoryRepository categoryRepository) {
        this.accessValidator = accessValidator;
        this.categoryValidator = categoryValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/categories/solution/{solutionId}/url/{url}")
    public Category getCategoryByUrl(@PathVariable("solutionId") long solutionId,
                                     @PathVariable("url") String url,
                                     @RequestHeader("apiToken") String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        Optional<Category> optCategory = categoryRepository.findBySolution_IdAndUrl(solutionId, url);

        if (optCategory.isPresent()) {
            Category page = optCategory.get();
            accessValidator.validateSolution(user, page.getSolution().getId());
            return page;
        }
        throw new NoSuchElementException();
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
