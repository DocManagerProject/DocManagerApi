package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.SettingsRepository;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.domain.user.User;
import pl.docmanager.web.security.AccessValidator;
import pl.docmanager.web.security.ApiTokenDecoder;

import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
public class SettingsRestController extends RestControllerBase {

    private SettingsRepository settingsRepository;
    private ApiTokenDecoder apiTokenDecoder;
    private AccessValidator accessValidator;

    @Autowired
    public SettingsRestController(AccessValidator accessValidator,
                                  ApiTokenDecoder apiTokenDecoder,
                                  SettingsRepository settingsRepository) {
        this.accessValidator = accessValidator;
        this.apiTokenDecoder = apiTokenDecoder;
        this.settingsRepository = settingsRepository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/api/settings/solution/{solutionId}/name/{name}")
    public Settings getSettingsBySolutionAndName(@PathVariable(name = "solutionId") long solutionId,
                                                 @PathVariable(name = "name") String name,
                                                 @RequestHeader("apiToken") String apiToken) {
        User user = apiTokenDecoder.getUseFromApiToken(apiToken);
        accessValidator.validateSolution(user, solutionId);
        Optional<Settings> settingsOpt = settingsRepository.findBySolution_IdAndName(solutionId, name);

        if (settingsOpt.isPresent()) {
            return settingsOpt.get();
        }

        throw new NoSuchElementException();
    }
}
