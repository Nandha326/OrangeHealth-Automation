package com.orangehealth.config;

// Author: Nandhakumar J
// Reads application configuration from config.json (testdata/config.json).
// Provides the base URL for the application under test.

import java.io.InputStream;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonConfigReader {

    // Classpath location of the JSON config file
    private static final String CONFIG_JSON = "testdata/config.json";

    // Holds all key-value pairs parsed from config.json
    private final Map<String, String> config;

    public JsonConfigReader() {
        // Load and parse config.json from the classpath
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_JSON)) {
            if (input == null) {
                throw new RuntimeException(CONFIG_JSON + " not found on classpath.");
            }
            config = new ObjectMapper().readValue(input, new TypeReference<Map<String, String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Unable to load " + CONFIG_JSON, e);
        }
    }

    // Returns the base URL of the application under test
    public String getBaseUrl() {
        String url = config.get("baseUrl");
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("baseUrl not defined in " + CONFIG_JSON);
        }
        return url;
    }
}
