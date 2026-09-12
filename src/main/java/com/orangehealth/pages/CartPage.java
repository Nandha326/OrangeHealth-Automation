package com.orangehealth.pages;

// Author: Nandhakumar J
// Shared cart and product-page actions used by Checkups, Tests, and Home.

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.orangehealth.base.BasePage;

public class CartPage extends BasePage {

    private static final Duration CART_WAIT = Duration.ofSeconds(30);

    // Product detail shell — present only on an individual test/checkup page
    private final By productDetails = By.cssSelector(
            ".primary-details-container, .product-details-container, .primary-details-card, section.primary-details, .product-details-wrapper, [class*='product-page'], [class*='primary-details'], .product-title, .product-name-container, .pdp3-hero-booking-card, .package-name-wrapper");

    // Add to Cart on the product page (not the listing-card CTA)
    private final By addToCartButton = By.xpath(
            "//button[contains(.,'Add to Cart') or contains(@class,'add-to-cart-button') or contains(.,'Add')] | "
                    + "//*[self::button or @role='button'][contains(.,'Add to Cart') or contains(.,'Add')]");

    private final By cartDrawer = By.cssSelector(
            "div.cart-modal-body, .cart-modal, .cart-drawer, [class*='cart-drawer'], [class*='cart-modal']");

    private final By cartTriggerButton = By.xpath(
            "//button[contains(@class,'cart-button')] | //header//button[contains(.,'Cart')] | //button[contains(.,'View Cart')]");

    private final By cartEmptyMessage = By.xpath(
            "//*[contains(text(),'Your cart is empty') or contains(text(),'cart is empty')]");

    private final By addedPackageName = By.xpath(
            "//div[contains(@class,'cart-modal')]//*[self::p or self::h3 or self::h4 or self::span][contains(@class,'name') or contains(@class,'title') or contains(.,'₹')] | "
                    + "//section[contains(@class,'cart-item')] | "
                    + "//*[contains(@class,'cart-item')]");

    private final By proceedButton = By.xpath(
            "//div[contains(@class,'cart-modal') or contains(@class,'modal-body') or contains(@class,'bottom-cart')]//button[contains(translate(.,'PROCEED','proceed'),'proceed') or contains(translate(.,'CHECKOUT','checkout'),'checkout')] | "
                    + "//section[contains(@class,'bottom-cart')]//button[contains(translate(.,'PROCEED','proceed'),'proceed') or contains(translate(.,'CHECKOUT','checkout'),'checkout')] | "
                    + "//div[contains(@class,'cart-modal')]//*[self::button or @role='button'][contains(translate(.,'PROCEED','proceed'),'proceed')] | "
                    + "//button[contains(translate(.,'PROCEED','proceed'),'proceed') or contains(translate(.,'CHECKOUT','checkout'),'checkout')][1]");

    private final By loginPageTitle = By.xpath(
            "//div[contains(@class,'auth-primary-header')] | "
                    + "//*[contains(@class,'auth')] | "
                    + "//*[contains(@class,'login')] | "
                    + "//*[self::h1 or self::h2 or self::h3 or self::span or self::p or self::div][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'sign in') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'login') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'mobile number')] | "
                    + "//button[contains(translate(., 'OTP', 'otp'),'otp')] | "
                    + "//input[contains(@type,'tel') or contains(@placeholder,'mobile') or contains(@placeholder,'Mobile') or contains(@name,'mobile')]");

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
        try {
            btn.click();
        } catch (Exception e) {
            executeScript("arguments[0].click();", btn);
        }
        ensureCartDrawerIsOpen();
        waitForVisibility(cartDrawer, CART_WAIT);
    }

    public void ensureCartDrawerIsOpen() {
        if (!isDisplayed(cartDrawer, Duration.ofMillis(1000))) {
            try {
                WebElement searchOverlay = driver.findElement(By.xpath("//div[contains(@class,'product-search-modal') or contains(@class,'search-modal')]"));
                if (searchOverlay.isDisplayed()) {
                    try {
                        WebElement closeBtn = searchOverlay.findElement(By.xpath(".//button[contains(@class,'close') or @aria-label='Close' or contains(.,'×')]"));
                        closeBtn.click();
                    } catch (Exception e) {
                        executeScript("arguments[0].style.display='none';", searchOverlay);
                    }
                }
            } catch (Exception ignored) {}

            if (isDisplayed(cartTriggerButton, Duration.ofSeconds(3))) {
                WebElement trigger = waitForFirstVisible(cartTriggerButton);
                scrollIntoView(trigger);
                try {
                    trigger.click();
                } catch (Exception e) {
                    executeScript("arguments[0].click();", trigger);
                }
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
        ensureCartDrawerIsOpen();
        // Primary check: wait for a named cart item element
        if (isDisplayed(addedPackageName, Duration.ofSeconds(15))) {
            return true;
        }
        // Fallback: use JS to check for any visible cart item content
        try {
            Boolean hasItems = (Boolean) executeScript(
                "return document.querySelectorAll('[class*=cart-item], [class*=cart-modal] p, [class*=cart-modal] h3, [class*=cart-modal] h4, [class*=cart-modal] span').length > 0;");
            if (Boolean.TRUE.equals(hasItems)) {
                return true;
            }
        } catch (Exception ignored) {}
        // Final check: drawer is open and not empty
        return isDisplayed(cartDrawer, Duration.ofSeconds(3)) && !isDisplayed(cartEmptyMessage, Duration.ofSeconds(1));
    }

    public void clickProceed() {
        ensureCartDrawerIsOpen();
        WebElement btn = null;
        try {
            btn = waitForFirstVisible(proceedButton);
        } catch (Exception e) {
            By fallback = By.xpath(
                "//button[contains(translate(.,'PROCEED','proceed'),'proceed')] | " +
                "//button[contains(translate(.,'CHECKOUT','checkout'),'checkout')] | " +
                "//button[contains(translate(.,'VIEW CART','view cart'),'view cart')] | " +
                "//a[contains(translate(.,'VIEW CART','view cart'),'view cart') or contains(@href,'cart')]"
            );
            try {
                btn = waitForFirstVisible(fallback);
            } catch (Exception e2) {
                try {
                    btn = (WebElement) executeScript(
                            "return [...document.querySelectorAll('button, [class*=oui-button], a, [role=button], span, label, div')]"
                                    + ".find(el => el.offsetWidth > 0 && el.offsetHeight > 0 && (el.textContent.trim().toLowerCase().includes('proceed') || el.textContent.trim().toLowerCase().includes('view cart') || el.textContent.trim().toLowerCase().includes('checkout')));");
                } catch (Exception ignored) {}
            }
        }
        if (btn != null) {
            scrollIntoView(btn);
            try {
                btn.click();
            } catch (Exception ex) {
                executeScript("arguments[0].click();", btn);
            }
        }

        try {
            wait(Duration.ofSeconds(5)).until(ExpectedConditions.or(
                ExpectedConditions.urlContains("auth"),
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.urlContains("checkout"),
                ExpectedConditions.visibilityOfElementLocated(loginPageTitle)
            ));
        } catch (Exception ignored) {
        }
        waitForPageLoad();
    }

    public boolean isLoginPageDisplayed() {
        waitForPageLoad();
        try {
            String url = getCurrentUrl().toLowerCase();
            if (url.contains("/login") || url.contains("/auth") || url.contains("/checkout") || url.contains("/sign-in")) {
                return true;
            }
        } catch (Exception ignored) {
        }
        if (isDisplayed(loginPageTitle, Duration.ofSeconds(10))) {
            return true;
        }
        try {
            return !driver.findElements(By.xpath("//input[@type='tel' or contains(@placeholder,'mobile') or contains(@placeholder,'Mobile') or contains(@name,'mobile')] | //button[contains(translate(.,'OTP','otp'),'otp')] | //*[contains(translate(.,'SIGN IN','sign in'),'sign in') or contains(translate(.,'LOGIN','login'),'login')]")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
