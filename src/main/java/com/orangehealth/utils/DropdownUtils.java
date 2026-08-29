package com.orangehealth.utils;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.orangehealth.base.DriverFactory;

/**
 * DropdownUtils
 *
 * Provides reusable methods for interacting with
 * HTML select elements and common custom dropdowns.
 *
 * @author Nandhakumar J
 */
@SuppressWarnings("null")
public final class DropdownUtils {

    private final WebDriver driver;

    private final WaitUtils waitUtils;

    public DropdownUtils() {

        this.driver = DriverFactory.getDriver();

        this.waitUtils = new WaitUtils();

    }

    /*=========================================================
     * HTML SELECT DROPDOWNS
     *========================================================*/

    public void selectByVisibleText(By locator,
                                    String text) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        select.selectByVisibleText(text);

    }

    public void selectByValue(By locator,
                              String value) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        select.selectByValue(value);

    }

    public void selectByIndex(By locator,
                              int index) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        select.selectByIndex(index);

    }

    public String getSelectedOption(By locator) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        return select.getFirstSelectedOption().getText();

    }

    public List<String> getAllOptions(By locator) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        List<String> values =
                new ArrayList<>();

        for (WebElement option : select.getOptions()) {

            values.add(option.getText());

        }

        return values;

    }

    public boolean isMultiple(By locator) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        return select.isMultiple();

    }

    public void deselectAll(By locator) {

        Select select =
                new Select(
                        waitUtils.waitForVisibility(locator));

        select.deselectAll();

    }

    /*=========================================================
     * CUSTOM DROPDOWNS
     *========================================================*/

    public void selectCustomDropdown(By dropdownLocator,
                                     By optionsLocator,
                                     String optionText) {

        waitUtils.waitForClickable(dropdownLocator)
                 .click();

        List<WebElement> options =
                driver.findElements(optionsLocator);

        for (WebElement option : options) {

            if (option.getText()
                      .trim()
                      .equalsIgnoreCase(optionText)) {

                option.click();

                return;

            }

        }

        throw new IllegalArgumentException(
                "Option not found : " + optionText);

    }

    /**
     * Searchable custom dropdown.
     */
    public void searchAndSelect(By searchLocator,
                                By optionsLocator,
                                String value) {

        WebElement search =
                waitUtils.waitForVisibility(searchLocator);

        search.clear();

        search.sendKeys(value);

        List<WebElement> options =
                driver.findElements(optionsLocator);

        for (WebElement option : options) {

            if (option.getText()
                      .trim()
                      .equalsIgnoreCase(value)) {

                option.click();

                return;

            }

        }

        throw new IllegalArgumentException(
                "Value not found : " + value);

    }

}