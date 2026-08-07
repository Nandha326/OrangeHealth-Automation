package com.orangehealth.utils;

import java.util.Map;

/**
 * TestDataContext
 *
 * Stores the current Excel test data for the executing thread.
 * This enables all Step Definition classes to access the same
 * test data without passing values between steps.
 */
public final class TestDataContext {

    /**
     * ThreadLocal ensures thread safety when tests run in parallel.
     */
    private static final ThreadLocal<Map<String, String>> TEST_DATA =
            new ThreadLocal<>();

    private TestDataContext() {

        throw new IllegalStateException("Utility class");

    }

    /**
     * Stores current test data.
     *
     * @param data Excel row as Map
     */
    public static void setTestData(
            Map<String, String> data) {

        TEST_DATA.set(data);

    }

    /**
     * Returns current test data.
     *
     * @return Map<String, String>
     */
    public static Map<String, String> getTestData() {

        return TEST_DATA.get();

    }

    /**
     * Returns a value by column name.
     *
     * Example:
     *
     * getValue("City")
     * getValue("Diagnostic Test")
     */
    public static String getValue(
            String columnName) {

        Map<String, String> data =
                TEST_DATA.get();

        if (data == null) {

            throw new IllegalStateException(
                    "No test data found. TestDataContext has not been initialized.");

        }

        return data.getOrDefault(columnName, "");

    }

    /**
     * Clears ThreadLocal after execution.
     */
    public static void clear() {

        TEST_DATA.remove();

    }

}