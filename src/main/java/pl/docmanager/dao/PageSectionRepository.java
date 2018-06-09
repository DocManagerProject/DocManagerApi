package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.page.PageSection;

public interface PageSectionRepository extends CrudRepository<PageSection, Long> {
}
