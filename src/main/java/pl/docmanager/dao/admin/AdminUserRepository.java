package pl.docmanager.dao.admin;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.admin.adminuser.AdminUser;

public interface AdminUserRepository extends CrudRepository<AdminUser, Long> {
}
