package com.orangehealth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * PropertyUtils
 *
 * Generic utility for loading and reading Java properties files.
 *
 * Thread-safe:
 * Each Properties instance is immutable after loading and
 * no mutable shared state is maintained.
 */
public final class PropertyUtils {

    private PropertyUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Load a properties file.
     */
    public static Properties load(Path propertyFile) {

        Objects.requireNonNull(propertyFile,
                "Property file path cannot be null.");

        Properties properties = new Properties();

        try (InputStream input =
                     Files.newInputStream(propertyFile)) {

            properties.load(input);

            return properties;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to load properties file: "
                            + propertyFile,
                    e);

        }

    }

    /**
     * Read String.
     */
    public static String getString(
            Properties properties,
            String key) {

        validate(properties, key);

        return properties.getProperty(key);

    }

    /**
     * Read String with default.
     */
    public static String getString(
            Properties properties,
            String key,
            String defaultValue) {

        validate(properties, key);

        return properties.getProperty(
                key,
                defaultValue);

    }

    /**
     * Read Integer.
     */
    public static int getInt(
            Properties properties,
            String key) {

        return Integer.parseInt(
                getString(properties, key));

    }

    /**
     * Read Long.
     */
    public static long getLong(
            Properties properties,
            String key) {

        return Long.parseLong(
                getString(properties, key));

    }

    /**
     * Read Double.
     */
    public static double getDouble(
            Properties properties,
            String key) {

        return Double.parseDouble(
                getString(properties, key));

    }

    /**
     * Read Boolean.
     */
    public static boolean getBoolean(
            Properties properties,
            String key) {

        return Boolean.parseBoolean(
                getString(properties, key));

    }

    /**
     * Check if key exists.
     */
    public static boolean containsKey(
            Properties properties,
            String key) {

        validate(properties, key);

        return properties.containsKey(key);

    }

    /**
     * Read Optional.
     */
    public static Optional<String> getOptional(
            Properties properties,
            String key) {

        validate(properties, key);

        return Optional.ofNullable(
                properties.getProperty(key));

    }

    /**
     * Validate arguments.
     */
    private static void validate(
            Properties properties,
            String key) {

        Objects.requireNonNull(
                properties,
                "Properties cannot be null.");

        Objects.requireNonNull(
                key,
                "Property key cannot be null.");

    }

}