package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;

import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class CategoryDao {

    private CategoryRepository categoryRepository;
    private CategoryValidator categoryValidator;

    @Autowired
    public CategoryDao(CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Category getCategoryByUrl(String url, long solutionId) {
        return categoryRepository.findBySolution_IdAndUrl(solutionId, url).orElseThrow(NoSuchElementException::new);
    }

    public void addCategory(Category category) {
        categoryValidator.validateCategory(category);
        categoryRepository.save(category);
    }

    public Category updateCategory(Map<String, Object> updatesMap, String url, long solutionId) {
        Category existingCategory = categoryRepository.findBySolution_IdAndUrl(solutionId, url).orElseThrow(NoSuchElementException::new);

        categoryValidator.validateLegalUpdate(updatesMap);
        if (updatesMap.containsKey("name")) {
            existingCategory.setName(updatesMap.get("name").toString());
        }

        if (updatesMap.containsKey("url")) {
            existingCategory.setUrl(updatesMap.get("url").toString());
        }

        return categoryRepository.save(existingCategory);
    }
}
