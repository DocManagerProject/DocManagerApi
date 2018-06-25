package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.category.CategoryDao;
import pl.docmanager.domain.category.Category;

import java.util.Map;

@RestController
public class CategoryRestController extends RestControllerBase {

    private CategoryDao categoryDao;

    @Autowired
    public CategoryRestController(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @GetMapping("/api/categories/solution/{solutionId}/url/{url}")
    public Category getCategoryByUrl(@PathVariable("solutionId") long solutionId,
                                     @PathVariable("url") String url,
                                     @RequestHeader("apiToken") String apiToken) {
        return categoryDao.getCategoryByUrl(url, solutionId, apiToken);
    }

    @PostMapping("/api/categories")
    public void addCategory(@RequestBody Category category, @RequestHeader("apiToken") String apiToken) {
        categoryDao.addCategory(category, apiToken);
    }

    @PatchMapping("/api/categories/solution/{solutionId}/url/{url}")
    public void updateCategory(@RequestBody Map<String, Object> updatesMap,
                               @PathVariable("solutionId") long solutionId,
                               @PathVariable("url") String url,
                               @RequestHeader("apiToken") String apiToken) {
        categoryDao.updateCategory(updatesMap, url, solutionId, apiToken);
    }
}
