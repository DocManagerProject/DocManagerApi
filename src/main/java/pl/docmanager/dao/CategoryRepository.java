package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.category.Category;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findBySolution_IdAndUrl(long solutionId, String url);
}
