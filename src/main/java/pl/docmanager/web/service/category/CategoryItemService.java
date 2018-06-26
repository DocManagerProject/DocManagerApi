package pl.docmanager.web.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.category.CategoryDao;
import pl.docmanager.dao.category.CategoryItemDao;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.List;

@Service
public class CategoryItemService {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private CategoryDao categoryDao;
    private CategoryItemDao categoryItemDao;

    @Autowired
    public CategoryItemService(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                               CategoryDao categoryDao, CategoryItemDao categoryItemDao) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryDao = categoryDao;
        this.categoryItemDao = categoryItemDao;
    }

    public List<CategoryItem> getAllByCategoryId(long categoryId, String apiToken) {
        Category category = categoryDao.getCategoryById(categoryId);
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, category.getSolution().getId());
        return categoryItemDao.getAllByCategoryId(categoryId);
    }
}
