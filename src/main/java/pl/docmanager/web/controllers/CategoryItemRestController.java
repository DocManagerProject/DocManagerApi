package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.category.CategoryItemRepository;
import pl.docmanager.dao.category.CategoryRepository;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class CategoryItemRestController extends RestControllerBase {

    private CategoryRepository categoryRepository;
    private CategoryItemRepository categoryItemRepository;
    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;

    @Autowired
    public CategoryItemRestController(AccessValidator accessValidator,
                                      CategoryRepository categoryRepository,
                                      CategoryItemRepository categoryItemRepository,
                                      ApiTokenDecoder apiTokenDecoder) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryRepository = categoryRepository;
        this.categoryItemRepository = categoryItemRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/category_items/category/{categoryId}")
    public List<CategoryItem> getAllCategoryItemsByCategoryId(@PathVariable("categoryId") long categoryId,
                                                              @RequestHeader("apiToken") String apiToken) {

        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        if (!optCategory.isPresent()) {
            throw new NoSuchElementException();
        }

        Category category = optCategory.get();
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, category.getSolution().getId());

        List<CategoryItem> ret = new ArrayList<>();
        categoryItemRepository.findAllByCategory_Id(categoryId).forEach(ret::add);
        return ret;
    }

}
