package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.Category;
import pl.docmanager.domain.category.CategoryItem;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryItemDao {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private CategoryDao categoryDao;
    private CategoryItemRepository categoryItemRepository;

    @Autowired
    public CategoryItemDao( AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                            CategoryDao categoryDao, CategoryItemRepository categoryItemRepository) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.categoryDao = categoryDao;
        this.categoryItemRepository = categoryItemRepository;
    }

    public List<CategoryItem> getAllByCategoryId(long categoryId, String apiToken) {
        Category category = categoryDao.getCategoryById(categoryId, apiToken);
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, category.getSolution().getId());

        List<CategoryItem> ret = new ArrayList<>();
        categoryItemRepository.findAllByCategory_Id(categoryId).forEach(ret::add);
        return ret;
    }
}
