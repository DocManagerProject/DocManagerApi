package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
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
}
