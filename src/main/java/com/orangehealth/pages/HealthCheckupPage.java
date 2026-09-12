package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Checkups page.
// Covers: navigation, full body checkup listing, checkup detail,
// add to cart, and proceed to login flow.

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehealth.base.BasePage;

public class HealthCheckupPage extends BasePage {

    private static final Duration PAGE_WAIT = Duration.ofSeconds(30);

    private final CartPage cartPage;

    public HealthCheckupPage(WebDriver driver) {
        super(driver);
        this.cartPage = new CartPage(driver);
    }

    /*=========================================================
     * Navigation
     *=========================================================*/

    private final By checkupsMenu =
            By.xpath("//nav//a[contains(normalize-space(.),'Checkups')] | //header//a[contains(normalize-space(.),'Checkups')] | //a[contains(normalize-space(.),'Checkups')]");

    /*=========================================================
     * Health Checkups Page
     *=========================================================*/

    private final By healthCheckupsPageTitle =
            By.cssSelector("h1.oui-typography.procedure-description, h1, [class*='procedure-description'], .category-checkup-page");

    private final By fullBodyCheckupsSection =
            By.xpath("//h2[contains(normalize-space(.),'Full body') or contains(normalize-space(.),'Full Body')]");

    private final By firstViewDetailsButton =
            By.xpath("(//h2[contains(normalize-space(.),'Full body') or contains(normalize-space(.),'Full Body')]/ancestor::section//a[contains(.,'View Details') or contains(.,'View details')] | "
                    + "//h2[contains(normalize-space(.),'Full body') or contains(normalize-space(.),'Full Body')]/following::a[contains(.,'View Details') or contains(.,'View details')] | "
                    + "//a[contains(@href,'checkup') and (contains(.,'View Details') or contains(.,'View details'))] | "
                    + "//a[contains(.,'View Details') or contains(.,'View details')])[1]");

    /*=========================================================
     * Business Methods
     *=========================================================*/

    public void clickCheckupsMenu() {
        try {
            WebElement menu = waitForClickable(checkupsMenu, Duration.ofSeconds(10));
            scrollIntoView(menu);
            menu.click();
        } catch (Exception e) {
            jsClick(checkupsMenu);
        }
        waitForPageLoad();
    }

    public boolean isHealthCheckupsPageDisplayed() {
        waitForPageLoad();
        try {
            String url = getCurrentUrl().toLowerCase();
            if (url.contains("checkup") || url.contains("package") || url.contains("category")) {
                return true;
            }
        } catch (Exception ignored) {}
        return isDisplayed(healthCheckupsPageTitle, PAGE_WAIT);
    }

    public void scrollToFullBodyCheckups() {
        scrollIntoView(fullBodyCheckupsSection);
    }

    public void clickFirstViewDetails() {
        click(firstViewDetailsButton);
        waitForPageLoad();
        cartPage.isProductDetailDisplayed();
    }

    public boolean isFullBodyCheckupDisplayed() {
        return cartPage.isProductDetailDisplayed();
    }

    public void addPackageToCart() {
        cartPage.addPackageToCart();
    }

    public boolean isCartDrawerDisplayed() {
        return cartPage.isCartDrawerDisplayed();
    }

    public boolean isSelectedPackageDisplayed() {
        return cartPage.isSelectedPackageDisplayed();
    }

    public void clickProceed() {
        cartPage.clickProceed();
    }

    public boolean isLoginPageDisplayed() {
        return cartPage.isLoginPageDisplayed();
    }
}

