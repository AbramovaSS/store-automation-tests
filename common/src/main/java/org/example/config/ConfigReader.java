package org.example.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

public class ConfigReader {
    public static final ProjectConfig CONFIG = ConfigFactory.create(ProjectConfig.class, System.getProperties());
}
