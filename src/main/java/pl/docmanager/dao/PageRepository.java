package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.page.Page;

import java.util.Optional;

public interface PageRepository extends CrudRepository<Page, Long> {
    Optional<Page> findBySolution_IdAndUrl(long solutionId, String url);
}
