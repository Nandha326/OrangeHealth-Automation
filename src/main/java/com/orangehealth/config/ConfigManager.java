package com.orangehealth.config;

// Author: Nandhakumar J
// Singleton that provides a single shared instance of ConfigReader
// across the entire framework lifecycle.

public final class ConfigManager {

    // Eagerly initialized singleton instance
    private static final ConfigManager INSTANCE = new ConfigManager();

    private final ConfigReader configReader;

    private ConfigManager() {
        // Initialize ConfigReader once at startup
        configReader = new ConfigReader();
    }

    // Returns the singleton ConfigManager instance
    public static ConfigManager getInstance() {
        return INSTANCE;
    }

    // Returns the shared ConfigReader instance
    public ConfigReader getConfigReader() {
        return configReader;
    }
}
