package com.orangehealth.testdata;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.orangehealth.utils.ExcelUtils;

public final class HomePageTestData {

    private static final String CLASSPATH_FILE = "testdata/TestData.xlsx";
    private static final String SHEET = "HomePage";
    private static final Map<String, String> DATA = load();

    private HomePageTestData() {
        throw new IllegalStateException("Utility class");
    }

    public static String get(String key) {
        String value = DATA.get(key);
        if (value == null) {
            throw new IllegalArgumentException(
                    "Test data key not found: " + key + ". Available keys: " + DATA.keySet());
        }
        return value;
    }

    public static String firstCity() {
        return DATA.keySet()
                .stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No city test data found in " + CLASSPATH_FILE));
    }

    private static Map<String, String> load() {
        try {
            InputStream input = HomePageTestData.class.getClassLoader().getResourceAsStream(CLASSPATH_FILE);
            if (input == null) {
                throw new IllegalStateException("Test data file not found on classpath: " + CLASSPATH_FILE);
            }
            Path tmp = Files.createTempFile("TestData", ".xlsx");
            tmp.toFile().deleteOnExit();
            Files.copy(input, tmp, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> map = new LinkedHashMap<>();
            List<List<String>> rows = ExcelUtils.readSheet(tmp, SHEET);

            for (int i = 1; i < rows.size(); i++) {
                List<String> row = rows.get(i);
                if (row.size() < 2) continue;
                String city = row.get(0).trim();
                String diagnosticTest = row.get(1).trim();
                if (!city.isEmpty() && !diagnosticTest.isEmpty()) {
                    map.put(city, diagnosticTest);
                }
            }

            if (map.isEmpty()) {
                throw new IllegalStateException("No valid test data found in " + CLASSPATH_FILE + " sheet " + SHEET);
            }

            return Collections.unmodifiableMap(map);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data from " + CLASSPATH_FILE, e);
        }
    }
}