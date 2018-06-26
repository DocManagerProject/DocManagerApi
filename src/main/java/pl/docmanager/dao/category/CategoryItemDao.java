package pl.docmanager.dao.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.category.CategoryItem;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryItemDao {

    private CategoryItemRepository categoryItemRepository;

    @Autowired
    public CategoryItemDao(CategoryItemRepository categoryItemRepository) {
        this.categoryItemRepository = categoryItemRepository;
    }

    public List<CategoryItem> getAllByCategoryId(long categoryId) {
        List<CategoryItem> ret = new ArrayList<>();
        categoryItemRepository.findAllByCategory_Id(categoryId).forEach(ret::add);
        return ret;
    }
}
