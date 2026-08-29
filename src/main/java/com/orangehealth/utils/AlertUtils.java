package com.orangehealth.utils;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import com.orangehealth.base.DriverFactory;

/**
 * AlertUtils
 *
 * Handles browser alert interactions.
 * Uses WaitUtils for synchronization and DriverFactory
 * for thread-safe WebDriver access.
 */
public final class AlertUtils {

    private final WebDriver driver;

    private final WaitUtils waitUtils;

    public AlertUtils() {

        this.driver = DriverFactory.getDriver();

        this.waitUtils = new WaitUtils();

    }

    /**
     * Wait and return alert.
     */
    private Alert getAlert() {

        return waitUtils.waitForAlert();

    }

    /**
     * Accept alert.
     */
    public void acceptAlert() {

        getAlert().accept();

    }

    /**
     * Dismiss alert.
     */
    public void dismissAlert() {

        getAlert().dismiss();

    }

    /**
     * Alert text.
     */
    public String getAlertText() {

        return getAlert().getText();

    }

    /**
     * Send value to prompt alert.
     */
    @SuppressWarnings("null")
    public void sendText(String text) {

        Alert alert = getAlert();

        alert.sendKeys(text);

    }

    /**
     * Accept prompt after entering text.
     */
    @SuppressWarnings("null")
    public void sendTextAndAccept(String text) {

        Alert alert = getAlert();

        alert.sendKeys(text);

        alert.accept();

    }

    /**
     * Dismiss prompt after entering text.
     */
    @SuppressWarnings("null")
    public void sendTextAndDismiss(String text) {

        Alert alert = getAlert();

        alert.sendKeys(text);

        alert.dismiss();

    }

    /**
     * Check whether alert is present.
     */
    public boolean isAlertPresent() {

        try {

            driver.switchTo().alert();

            return true;

        } catch (NoAlertPresentException e) {

            return false;

        }

    }

}