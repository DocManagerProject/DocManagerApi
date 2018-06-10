package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.CategoryRepository;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.controllers.validation.CategoryValidator;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class CategoryRestController extends RestControllerBase {

    private CategoryValidator categoryValidator;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryRestController(AccessValidator accessValidator, CategoryValidator categoryValidator,
                                  ApiTokenDecoder apiTokenDecoder, CategoryRepository categoryRepository) {
        super(accessValidator, apiTokenDecoder);
        this.categoryValidator = categoryValidator;
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

    @RequestMapping(method = RequestMethod.POST, path = "/api/categories")
    public void addCategory(@RequestBody Category category, @RequestHeader("apiToken") String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        categoryValidator.validateCategory(category);
        accessValidator.validateSolution(user, category.getSolution().getId());
        category.setAuthor(user);
        category.setCreateDate(LocalDateTime.now());
        category.setState(CategoryState.ACTIVE);
        categoryRepository.save(category);
    }
}
