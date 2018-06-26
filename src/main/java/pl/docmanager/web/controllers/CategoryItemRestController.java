package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.web.service.category.CategoryItemService;

import java.util.List;

@RestController
public class CategoryItemRestController extends RestControllerBase {

    private CategoryItemService categoryItemService;

    @Autowired
    public CategoryItemRestController(CategoryItemService categoryItemService) {
        this.categoryItemService = categoryItemService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/category_items/category/{categoryId}")
    public List<CategoryItem> getAllCategoryItemsByCategoryId(@PathVariable("categoryId") long categoryId,
                                                              @RequestHeader("apiToken") String apiToken) {
        return categoryItemService.getAllByCategoryId(categoryId, apiToken);
    }
}
