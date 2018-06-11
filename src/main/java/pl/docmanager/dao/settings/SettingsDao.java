package pl.docmanager.dao.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SettingsDao {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsDao(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                       SettingsRepository settingsRepository) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.settingsRepository = settingsRepository;
    }

    public Settings getSettingsByName(String name, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, solutionId);
        Optional<Settings> settingsOpt = settingsRepository.findBySolution_IdAndName(solutionId, name);

        if (settingsOpt.isPresent()) {
            return settingsOpt.get();
        }

        throw new NoSuchElementException();
    }
}
