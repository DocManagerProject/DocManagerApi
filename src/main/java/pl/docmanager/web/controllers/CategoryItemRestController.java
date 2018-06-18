package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.category.CategoryItemDao;
import pl.docmanager.domain.category.CategoryItem;

import java.util.List;

@RestController
public class CategoryItemRestController extends RestControllerBase {

    private CategoryItemDao categoryItemDao;

    @Autowired
    public CategoryItemRestController(CategoryItemDao categoryItemDao) {
        this.categoryItemDao = categoryItemDao;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/category_items/category/{categoryId}")
    public List<CategoryItem> getAllCategoryItemsByCategoryId(@PathVariable("categoryId") long categoryId,
                                                              @RequestHeader("apiToken") String apiToken) {
        return categoryItemDao.getAllByCategoryId(categoryId, apiToken);
    }
}
