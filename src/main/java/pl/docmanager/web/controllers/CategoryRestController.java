package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.domain.category.Category;
import pl.docmanager.web.service.category.CategoryService;

import java.util.Map;

@RestController
public class CategoryRestController extends RestControllerBase {

    private CategoryService categoryService;

    @Autowired
    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/api/categories/solution/{solutionId}/url/{url}")
    public Category getCategoryByUrl(@PathVariable("solutionId") long solutionId,
                                     @PathVariable("url") String url,
                                     @RequestHeader("apiToken") String apiToken) {
        return categoryService.getCategoryByUrl(url, solutionId, apiToken);
    }

    @PostMapping("/api/categories")
    public void addCategory(@RequestBody Category category, @RequestHeader("apiToken") String apiToken) {
        categoryService.addCategory(category, apiToken);
    }

    @PatchMapping("/api/categories/solution/{solutionId}/url/{url}")
    public void updateCategory(@RequestBody Map<String, Object> updatesMap,
                               @PathVariable("solutionId") long solutionId,
                               @PathVariable("url") String url,
                               @RequestHeader("apiToken") String apiToken) {
        categoryService.updateCategory(updatesMap, url, solutionId, apiToken);
    }
}
