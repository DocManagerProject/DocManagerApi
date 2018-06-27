package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryItemDao {

    private CategoryItemRepository categoryItemRepository;
    private CategoryItemValidator categoryItemValidator;

    @Autowired
    public CategoryItemDao(CategoryItemRepository categoryItemRepository,
                           CategoryItemValidator categoryItemValidator) {
        this.categoryItemRepository = categoryItemRepository;
        this.categoryItemValidator = categoryItemValidator;
    }

    public List<CategoryItem> getAllByCategoryId(long categoryId) {
        List<CategoryItem> ret = new ArrayList<>();
        categoryItemRepository.findAllByCategory_Id(categoryId).forEach(ret::add);
        return ret;
    }

    public List<CategoryItem> getAllByContentPageId(long contentPageId) {
        List<CategoryItem> ret = new ArrayList<>();
        categoryItemRepository.findAllByContentPage_Id(contentPageId).forEach(ret::add);
        return ret;
    }

    public void addAll(List<CategoryItem> categoryItems) {
        categoryItems.forEach(categoryItemValidator::validateCategoryItem);
        categoryItemRepository.saveAll(categoryItems);
    }

    public void removeAll(List<CategoryItem> categoryItems) {
        categoryItemRepository.deleteAll(categoryItems);
    }
}
