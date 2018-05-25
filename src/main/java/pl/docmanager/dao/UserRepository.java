package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
