package pl.docmanager.domain.global;

import pl.docmanager.domain.solution.Solution;

public class SettingsBuilder {
    private long id;
    private Solution solution;
    private String name;
    private String value;
    private String domain;

    public SettingsBuilder(long id, Solution solution) {
        this.id = id;
        this.solution = solution;
    }

    public SettingsBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SettingsBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public SettingsBuilder withDomain(String domain) {
        this.domain = domain;
        return this;
    }

    public Settings build() {
        Settings settings = new Settings();
        settings.setId(id);
        settings.setSolution(solution);
        settings.setName(name);
        settings.setValue(value);
        settings.setDomain(domain);
        return settings;
    }
}
