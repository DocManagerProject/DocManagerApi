package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.page.Page;

public interface PageRepository extends CrudRepository<Page, Long> {
}
