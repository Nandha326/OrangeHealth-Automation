package com.orangehealth.constants;

public final class MessageConstants {

    private MessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DRIVER_INITIALIZED =
            "WebDriver initialized successfully.";

    public static final String DRIVER_QUIT =
            "WebDriver closed successfully.";

    public static final String ELEMENT_NOT_FOUND =
            "Element not found.";

    public static final String INVALID_BROWSER =
            "Unsupported browser configured.";

    public static final String CONFIG_NOT_FOUND =
            "Configuration file not found.";

    public static final String SCREENSHOT_CAPTURED =
            "Screenshot captured successfully.";
}