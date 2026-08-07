package com.orangehealth.reports;

import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.aventstack.extentreports.MediaEntityBuilder;

/**
 * ReportAttachments
 *
 * Handles attachment of screenshots and other artifacts
 * to the current ExtentTest.
 */
public final class ReportAttachments {

    private ReportAttachments() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Attach screenshot to report.
     *
     * @param absoluteScreenshotPath Absolute path returned by ScreenshotManager
     */
    public static void attachScreenshot(String absoluteScreenshotPath) throws IOException {

        if (ExtentManager.getTest() == null) {
            return;
        }

        String relativePath =
                convertToRelativePath(absoluteScreenshotPath);

        ExtentManager.getTest().info(
                "Screenshot",
                MediaEntityBuilder
                        .createScreenCaptureFromPath(relativePath)
                        .build());

    }

    /**
     * Attach screenshot with custom message.
     */
    public static void attachScreenshot(String message,
                                        String absoluteScreenshotPath) throws IOException {

        if (ExtentManager.getTest() == null) {
            return;
        }

        String relativePath =
                convertToRelativePath(absoluteScreenshotPath);

        ExtentManager.getTest().info(
                message,
                MediaEntityBuilder
                        .createScreenCaptureFromPath(relativePath)
                        .build());

    }

    /**
     * Convert absolute path into report-compatible relative path.
     */
    private static String convertToRelativePath(String absolutePath) {

        Path reportDirectory =
                Paths.get("reports")
                        .toAbsolutePath()
                        .normalize();

        Path screenshot =
                Paths.get(absolutePath)
                        .toAbsolutePath()
                        .normalize();

        return reportDirectory
                .relativize(screenshot)
                .toString()
                .replace(File.separator, "/");

    }

}
