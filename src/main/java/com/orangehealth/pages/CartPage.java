package com.orangehealth.pages;

// Author: Nandhakumar J
// Shared cart and product-page actions used by Checkups, Tests, and Home.

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehealth.base.BasePage;

public class CartPage extends BasePage {

    private static final Duration CART_WAIT = Duration.ofSeconds(10);

    // Product detail shell — present only on an individual test/checkup page
    private final By productDetails = By.cssSelector(
            ".primary-details-container, .product-details-container, .primary-details-card, section.primary-details, .product-details-wrapper, [class*='product-page'], [class*='primary-details'], .product-title, .product-name-container, .pdp3-hero-booking-card");

    // Add to Cart on the product page (not the listing-card CTA)
    private final By addToCartButton = By.xpath(
            "//button[contains(.,'Add to Cart') or contains(@class,'add-to-cart-button')] | "
                    + "//*[self::button or @role='button'][contains(.,'Add to Cart')]");

    private final By cartDrawer = By.cssSelector(
            "div.cart-modal-body, .cart-modal, [class*='cart-drawer'], [class*='cart-modal']");

    private final By cartTriggerButton = By.xpath(
            "//button[contains(@class,'cart-button')] | //header//button[contains(.,'Cart')] | //button[contains(.,'View Cart')]");

    private final By cartEmptyMessage = By.xpath(
            "//*[contains(text(),'Your cart is empty') or contains(text(),'cart is empty')]");

    private final By addedPackageName = By.xpath(
            "//div[contains(@class,'cart-modal')]//*[self::p or self::h3 or self::h4 or self::span][contains(@class,'name') or contains(@class,'title') or contains(.,'₹')] | "
                    + "//section[contains(@class,'cart-item')] | "
                    + "//*[contains(@class,'cart-item')]");

    private final By proceedButton = By.xpath(
            "//div[contains(@class,'cart-modal') or contains(@class,'modal-body') or contains(@class,'bottom-cart')]//button[contains(.,'Proceed')] | "
                    + "//section[contains(@class,'bottom-cart')]//button[contains(.,'Proceed')] | "
                    + "//div[contains(@class,'cart-modal')]//*[self::button or @role='button'][contains(.,'Proceed')]");

    private final By loginPageTitle = By.xpath(
            "//div[contains(@class,'auth-primary-header')] | "
                    + "//*[contains(@class,'auth')] | "
                    + "//*[self::h1 or self::h2 or self::span or self::p or self::div][contains(normalize-space(.),'Sign in') or contains(normalize-space(.),'Login') or contains(.,'verify your mobile number')] | "
                    + "//button[contains(.,'Get OTP')]");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public boolean isProductDetailDisplayed() {
        waitForPageLoad();
        return isDisplayed(productDetails, CART_WAIT);
    }

    public void addPackageToCart() {
        WebElement btn = waitForFirstVisible(addToCartButton);
        scrollIntoView(btn);
        pause(Duration.ofMillis(500));
        try {
            btn.click();
        } catch (Exception e) {
            executeScript("arguments[0].click();", btn);
        }
        pause(Duration.ofMillis(1500));
        ensureCartDrawerIsOpen();
        waitForVisibility(cartDrawer, CART_WAIT);
    }

    public void ensureCartDrawerIsOpen() {
        if (!isDisplayed(cartDrawer, Duration.ofMillis(1000))) {
            if (isDisplayed(cartTriggerButton, Duration.ofSeconds(3))) {
                WebElement trigger = waitForFirstVisible(cartTriggerButton);
                scrollIntoView(trigger);
                try {
                    trigger.click();
                } catch (Exception e) {
                    executeScript("arguments[0].click();", trigger);
                }
                pause(Duration.ofMillis(1500));
            }
        }
    }

    public boolean isCartDrawerDisplayed() {
        ensureCartDrawerIsOpen();
        return isDisplayed(cartDrawer, CART_WAIT);
    }

    public boolean isCartDrawerOpen() {
        return isDisplayed(cartDrawer, Duration.ofSeconds(1));
    }

    public boolean isSelectedPackageDisplayed() {
        if (isDisplayed(cartEmptyMessage, Duration.ofMillis(500))) {
            return false;
        }
        return isDisplayed(addedPackageName, CART_WAIT);
    }

    public void clickProceed() {
        WebElement btn = waitForFirstVisible(proceedButton);
        scrollIntoView(btn);
        pause(Duration.ofMillis(500));
        try {
            btn.click();
        } catch (Exception e) {
            executeScript("arguments[0].click();", btn);
        }
        pause(Duration.ofMillis(1500));
    }

    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginPageTitle, CART_WAIT);
    }
}



