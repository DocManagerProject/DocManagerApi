package pl.docmanager.dao.category;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.category.CategoryItem;

public interface CategoryItemRepository extends CrudRepository<CategoryItem, Long> {
    Iterable<CategoryItem> findAllByCategory_Id(long categoryId);
    Iterable<CategoryItem> findAllByContentPage_Id(long contentPageId);
}
