package pl.docmanager.web.service.category;

import org.springframework.stereotype.Service;
import pl.docmanager.dao.category.CategoryDao;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryState;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CategoryService {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private CategoryDao categoryDao;

    public CategoryService(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                           CategoryDao categoryDao) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryDao = categoryDao;
    }

    public Category getCategoryById(long id, long solutionId, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        Category category = categoryDao.getCategoryById(id);
        accessValidator.validateSolution(requester, solutionId);
        accessValidator.validateSolution(requester, category.getSolution().getId());
        return category;
    }

    public Category getCategoryByUrl(String url, long solutionId, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(requester, solutionId);
        return categoryDao.getCategoryByUrl(url, solutionId);
    }

    public void addCategory(Category category, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        if (category.getSolution() == null) {
            throw new IllegalArgumentException("Category's solution cannot be null");
        }
        accessValidator.validateSolution(requester, category.getSolution().getId());
        category.setAuthor(requester);
        category.setCreateDate(LocalDateTime.now());
        category.setState(CategoryState.ACTIVE);
        categoryDao.addCategory(category);
    }

    public Category updateCategory(Map<String, Object> updatesMap, String url, long solutionId, String apiToken) {
        User requester = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(requester, solutionId);
        return categoryDao.updateCategory(updatesMap, url, solutionId);
    }
}
