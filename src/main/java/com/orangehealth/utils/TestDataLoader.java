package com.orangehealth.utils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import com.orangehealth.constants.PathConstants;

/**
 * TestDataLoader
 *
 * Loads test data from Excel for the current scenario.
 */
public final class TestDataLoader {

    /**
     * Excel file location.
     */
    private static final Path EXCEL_FILE =
            Paths.get(PathConstants.TEST_DATA_PATH + "TestData.xlsx");

    /**
     * Sheet name.
     */
    private static final String SHEET_NAME =
            "HomePage";

    private TestDataLoader() {

        throw new IllegalStateException(
                "Utility class");

    }

    /**
     * Load a specific row from Excel.
     *
     * Row 0 = Header
     * Row 1 = First Test Data
     * Row 2 = Second Test Data
     * ...
     *
     * @param rowNumber Excel row number
     * @return Map<String, String>
     */
    public static Map<String, String> loadTestData(
            int rowNumber) {

        return ExcelUtils.readRowAsMap(
                EXCEL_FILE,
                SHEET_NAME,
                rowNumber);

    }

}
