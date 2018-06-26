package pl.docmanager.dao.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.global.Settings;

import java.util.NoSuchElementException;

@Service
public class SettingsDao {

    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsDao(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public Settings getSettingsByName(String name, long solutionId) {
        return settingsRepository.findBySolution_IdAndName(solutionId, name).orElseThrow(NoSuchElementException::new);
    }
}
