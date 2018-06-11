package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.dao.settings.SettingsDao;
import pl.docmanager.domain.global.Settings;

@RestController
public class SettingsRestController extends RestControllerBase {

    private SettingsDao settingsDao;

    @Autowired
    public SettingsRestController(SettingsDao settingsDao) {
        this.settingsDao = settingsDao;
    }

    @GetMapping("/api/settings/solution/{solutionId}/name/{name}")
    public Settings getSettingsBySolutionAndName(@PathVariable(name = "solutionId") long solutionId,
                                                 @PathVariable(name = "name") String name,
                                                 @RequestHeader("apiToken") String apiToken) {
        return settingsDao.getSettingsByName(name, solutionId, apiToken);
    }
}
