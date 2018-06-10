package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.category.CategoryItem;

public interface CategoryItemRepository extends CrudRepository<CategoryItem, Long> {
    Iterable<CategoryItem> findAllByCategory_Id(long categoryId);
}
