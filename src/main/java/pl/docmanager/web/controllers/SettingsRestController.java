package pl.docmanager.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.docmanager.domain.global.Settings;
import pl.docmanager.web.service.settings.SettingsService;

@RestController
public class SettingsRestController extends RestControllerBase {

    private SettingsService settingsService;

    @Autowired
    public SettingsRestController(SettingsService settingsService) {
        this.settingsService = settingsService;
    }

    @GetMapping("/api/settings/solution/{solutionId}/name/{name}")
    public Settings getSettingsBySolutionAndName(@PathVariable(name = "solutionId") long solutionId,
                                                 @PathVariable(name = "name") String name,
                                                 @RequestHeader("apiToken") String apiToken) {
        return settingsService.getSettingsByName(name, solutionId, apiToken);
    }
}
