package com.orangehealth.constants;

public final class PathConstants {

    private PathConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String RESOURCE_PATH =
            "src/test/resources/";

    public static final String FEATURE_PATH =
            RESOURCE_PATH + "features/";

    public static final String CONFIG_PATH =
            RESOURCE_PATH + "config/";

    public static final String TEST_DATA_PATH =
            RESOURCE_PATH + "testdata/";

    public static final String REPORT_PATH =
            "reports/";

    public static final String SCREENSHOT_PATH =
            "screenshots/";

    public static final String LOG_PATH =
            "logs/";
}