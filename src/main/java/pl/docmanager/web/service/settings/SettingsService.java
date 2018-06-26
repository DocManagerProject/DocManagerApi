package pl.docmanager.web.service.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.docmanager.dao.settings.SettingsDao;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

@Service
public class SettingsService {

    private AccessValidator accessValidator;
    private ApiTokenDecoder apiTokenDecoder;
    private SettingsDao settingsDao;

    @Autowired
    public SettingsService(AccessValidator accessValidator, ApiTokenDecoder apiTokenDecoder,
                           SettingsDao settingsDao) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.settingsDao = settingsDao;
    }

    public Settings getSettingsByName(String name, long solutionId, String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, solutionId);
        return settingsDao.getSettingsByName(name, solutionId);
    }

}
