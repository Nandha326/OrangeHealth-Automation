package com.orangehealth.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * JsonUtils
 *
 * Utility class for JSON read/write and object mapping.
 *
 * Thread-safe:
 * ObjectMapper is configured once and reused.
 */
public final class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper()
                    .findAndRegisterModules()
                    .enable(SerializationFeature.INDENT_OUTPUT);

    private JsonUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Read JSON file into POJO.
     */
    public static <T> T read(Path jsonFile,
                             Class<T> clazz) {

        try {

            return OBJECT_MAPPER.readValue(
                    jsonFile.toFile(),
                    clazz);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read JSON file: " + jsonFile,
                    e);

        }

    }

    /**
     * Read JSON string into POJO.
     */
    public static <T> T read(String json,
                             Class<T> clazz) {

        try {

            return OBJECT_MAPPER.readValue(
                    json,
                    clazz);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to parse JSON string.",
                    e);

        }

    }

    /**
     * Read JSON array into List.
     */
    public static <T> List<T> readList(
            Path jsonFile,
            TypeReference<List<T>> typeReference) {

        try {

            return OBJECT_MAPPER.readValue(
                    jsonFile.toFile(),
                    typeReference);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read JSON list.",
                    e);

        }

    }

    /**
     * Read JSON into Map.
     */
    public static Map<String, Object> readMap(
            Path jsonFile) {

        try {

            return OBJECT_MAPPER.readValue(
                    jsonFile.toFile(),
                    new TypeReference<Map<String, Object>>() {
                    });

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to read JSON map.",
                    e);

        }

    }

    /**
     * Convert POJO to JSON string.
     */
    public static String toJson(Object object) {

        try {

            return OBJECT_MAPPER.writeValueAsString(
                    object);

        } catch (JsonProcessingException e) {

            throw new RuntimeException(
                    "Unable to serialize object.",
                    e);

        }

    }

    /**
     * Write POJO to JSON file.
     */
    public static void write(Path jsonFile,
                             Object object) {

        try {

            Files.createDirectories(
                    jsonFile.getParent());

            OBJECT_MAPPER.writeValue(
                    jsonFile.toFile(),
                    object);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to write JSON file.",
                    e);

        }

    }

    /**
     * Pretty print JSON string.
     */
    public static String prettyPrint(
            String json) {

        try {

            Object object =
                    OBJECT_MAPPER.readValue(
                            json,
                            Object.class);

            return OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to pretty print JSON.",
                    e);

        }

    }

    /**
     * Validate JSON.
     */
    public static boolean isValid(
            String json) {

        try {

            OBJECT_MAPPER.readTree(json);

            return true;

        } catch (IOException e) {

            return false;

        }

    }

}