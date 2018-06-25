package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryDao {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private CategoryRepository categoryRepository;
    private CategoryValidator categoryValidator;

    @Autowired
    public CategoryDao(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                       CategoryRepository categoryRepository, CategoryValidator categoryValidator) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryRepository = categoryRepository;
        this.categoryValidator = categoryValidator;
    }

    public Category getCategoryById(long id, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        Optional<Category> optCategory = categoryRepository.findById(id);

        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            accessValidator.validateSolution(user, category.getSolution().getId());
            return category;
        }
        throw new NoSuchElementException();
    }

    public Category getCategoryByUrl(String url, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        Optional<Category> optCategory = categoryRepository.findBySolution_IdAndUrl(solutionId, url);

        if (optCategory.isPresent()) {
            Category category = optCategory.get();
            accessValidator.validateSolution(user, category.getSolution().getId());
            return category;
        }
        throw new NoSuchElementException();
    }

    public void addCategory(Category category, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        categoryValidator.validateCategory(category);
        accessValidator.validateSolution(user, category.getSolution().getId());
        category.setAuthor(user);
        category.setCreateDate(LocalDateTime.now());
        category.setState(CategoryState.ACTIVE);
        categoryRepository.save(category);
    }

    public Category updateCategory(Map<String, Object> updatesMap, String url, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, solutionId);

        Optional<Category> existingCategoryOpt = categoryRepository.findBySolution_IdAndUrl(solutionId, url);
        Category existingCategory = existingCategoryOpt.orElseThrow(NoSuchElementException::new);

        categoryValidator.validateLegalUpdate(user, existingCategory, updatesMap);
        if (updatesMap.containsKey("name")) {
            existingCategory.setName(updatesMap.get("name").toString());
        }

        if (updatesMap.containsKey("url")) {
            existingCategory.setUrl(updatesMap.get("url").toString());
        }

        categoryRepository.save(existingCategory);
        return existingCategory;
    }
}
