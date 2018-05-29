package pl.docmanager.dao;

import org.springframework.data.repository.CrudRepository;
import pl.docmanager.domain.global.Settings;

import java.util.Optional;

public interface SettingsRepository extends CrudRepository<Settings, Long> {
    Optional<Settings> findBySolution_IdAndName(long solutionId, String name);
}
