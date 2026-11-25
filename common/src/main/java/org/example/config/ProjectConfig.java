package org.example.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "system:properties",
        "classpath:config.properties"
})

public interface ProjectConfig extends Config {
    @Key("baseUrl")
    @DefaultValue("http://localhost:8080")
    String baseUrl();

    @Key("basePath")
    @DefaultValue("/goods")
    String basePath();

    @Key("browserSize")
    @DefaultValue("1920x1080")
    String browserSize();

    @Key("browserName")
    @DefaultValue("chrome")
    String browserName();

    @Key("userLogin")
    @DefaultValue("admin")
    String userLogin();

    @Key("userPassword")
    @DefaultValue("secret123")
    String userPassword();

    @Key("timeout")
    @DefaultValue("4000")
    long timeout();

    @Key("enableDetailedLogging")
    @DefaultValue("false")
    boolean enableDetailedLogging();
}
