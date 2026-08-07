package com.orangehealth.reports;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.constants.PathConstants;

public final class ScreenshotManager {

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotManager() {
        throw new IllegalStateException("Utility class");
    }

    public static String captureScreenshot(String screenshotName) {
        WebDriver driver = DriverFactory.getDriver();

        if (!(driver instanceof TakesScreenshot)) {
            throw new IllegalStateException("Current WebDriver does not support screenshots.");
        }

        Path screenshotDirectory = Path.of(PathConstants.SCREENSHOT_PATH);
        String fileName = sanitizeFileName(screenshotName)
                + "_"
                + LocalDateTime.now().format(TIMESTAMP_FORMAT)
                + ".png";

        Path destination = screenshotDirectory.resolve(fileName);

        try {
            Files.createDirectories(screenshotDirectory);

            File source =
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Files.copy(
                    source.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Unable to capture screenshot.", e);
        }

        return destination.toAbsolutePath().toString();
    }

    private static String sanitizeFileName(String value) {
        return value == null || value.isBlank()
                ? "screenshot"
                : value.replaceAll("[^a-zA-Z0-9._-]+", "_");
    }
}
