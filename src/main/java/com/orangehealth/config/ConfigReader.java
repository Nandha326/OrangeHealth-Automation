package com.orangehealth.config;

// Author: Nandhakumar J
// Reads all configuration values from config.properties.
// Base URL is delegated to JsonConfigReader (config.json).

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final String CONFIG_FILE = "config/config.properties";

    private final Properties properties;

    public ConfigReader() {

        properties = new Properties();

        // Load config.properties from the classpath
        try (InputStream input =
                getClass().getClassLoader()
                        .getResourceAsStream(CONFIG_FILE)) {

            if (input == null) {
                throw new RuntimeException(CONFIG_FILE + " not found.");
            }

            properties.load(input);

        } catch (IOException e) {

            throw new RuntimeException("Unable to load configuration file.", e);

        }

    }

    // Reads baseUrl from config.json via JsonConfigReader
    public String getBaseUrl() {
        return new JsonConfigReader().getBaseUrl();
    }

    // Returns the browser name; defaults to chrome if not set
    public String getBrowser() {
        return getString("browser", "chrome");
    }

    // Returns true if headless mode is enabled
    public boolean isHeadless() {
        return getBoolean("headless", false);
    }

    // Returns implicit wait timeout in seconds
    public int getImplicitWait() {
        return getInt("implicit.wait", 0);
    }

    // Returns explicit wait timeout in seconds
    public int getExplicitWait() {
        return getInt("explicit.wait", 20);
    }

    // Returns page load timeout in seconds
    public int getPageLoadTimeout() {
        return getInt("page.load.timeout", 60);
    }

    // Returns script execution timeout in seconds
    public int getScriptTimeout() {
        return getInt("script.timeout", 30);
    }

    // Returns true if the browser window should be maximized on launch
    public boolean isMaximizeWindow() {
        return getBoolean("maximize.window", true);
    }

    // Returns true if cookies should be deleted before each test
    public boolean isDeleteCookies() {
        return getBoolean("delete.cookies", true);
    }

    // Returns true if a screenshot should be captured on test failure
    public boolean isScreenshotOnFail() {
        return getBoolean("screenshot.on.fail", true);
    }

    // Returns true if a screenshot should be captured on test pass
    public boolean isScreenshotOnPass() {
        return getBoolean("screenshot.on.pass", false);
    }

    // Returns true if parallel execution is enabled
    public boolean isParallelExecution() {
        return getBoolean("parallel.execution", false);
    }

    // Returns the number of parallel threads
    public int getThreadCount() {
        return getInt("thread.count", 1);
    }

    // Returns the string value for a key, or the defaultValue if blank/missing
    private String getString(String key, String defaultValue) {
        String value = resolve(key);
        return value == null || value.isBlank()
                ? defaultValue
                : value.trim();
    }

    // Parses an integer config value; throws if the value is not a valid integer
    private int getInt(String key, int defaultValue) {
        String value = getString(key, String.valueOf(defaultValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Configuration value for " + key + " must be an integer. Actual value: " + value,
                    e);
        }
    }

    // Parses a boolean config value
    private boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(getString(key, String.valueOf(defaultValue)));
    }

    // Resolves a config value: system property > environment variable > properties file
    private String resolve(String key) {
        // 1. Check JVM system property (-Dkey=value)
        String systemValue = System.getProperty(key);
        if (systemValue != null) {
            return systemValue;
        }

        // 2. Check OS environment variable (KEY_NAME format)
        String envValue = System.getenv(key.toUpperCase().replace('.', '_'));
        if (envValue != null) {
            return envValue;
        }

        // 3. Fall back to config.properties
        return properties.getProperty(key);
    }
}
