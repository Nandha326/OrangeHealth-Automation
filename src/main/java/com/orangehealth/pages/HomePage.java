package com.orangehealth.pages;

// Author: Nandhakumar J
// Page Object for the Orange Health Home Page.
// Covers: logo, location selection, search bar, segmented controls,
// search results, add to cart, and proceed to login flow.

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.orangehealth.base.BasePage;

/**
 * HomePage
 *
 * Page Object representing the Orange Health home page and its interactions.
 *
 * @author Nandhakumar J
 */
public class HomePage extends BasePage {

    private static final Duration SHORT_WAIT = Duration.ofSeconds(2);
    private static final Duration POPUP_CLOSE_WAIT = Duration.ofSeconds(3);
    private static final Duration FAST_CHECK = Duration.ofSeconds(2);
    private static final Duration PAGE_WAIT = Duration.ofSeconds(30);

    /*=========================================================
     * Locators
     *=========================================================*/

    private final By orangeHealthLogo = By.xpath("//header//img[@alt='Orange Health'] | //img[contains(@alt,'Orange Health')]");
    private final By myLocationMenu = By.cssSelector("button.location-picker-button");
    private final By selectedCity = By.cssSelector("button.location-picker-button");

    private final By locationPopup = By.xpath(
            "//div[contains(@class,'city-selector-modal') and @data-state='open']"
            + " | //div[@data-state='open' and .//*[contains(text(),'Select') and contains(text(),'city')]]"
            + " | //div[contains(@class,'oui-modal--overlay') and .//div[contains(@class,'city-selector-modal')]]"
            + " | //div[contains(@class,'city-selector-modal') and not(@data-state='closed')]"
            + " | //div[contains(@role,'dialog') and .//*[contains(text(),'Select') and contains(text(),'city')]]");

    private final By locationPopupCloseButton = By.xpath(
            "//*[contains(@role,'dialog') or contains(@class,'modal')]//button[contains(translate(@aria-label,'CLOSE','close'),'close') or contains(translate(@class,'CLOSE','close'),'close')]");

    private final By locationOverlay = By.cssSelector("div.oui-modal--overlay");

    private final By searchBar = By.xpath(
            "//div[contains(@class,'fake-search-input') and not(contains(@style,'pointer-events: none')) and not(contains(@style,'pointer-events:none')) and not(contains(@style,'opacity: 0')) and not(contains(@style,'opacity:0'))]"
            + " | //div[contains(@class,'fake-search-input')]"
            + " | //*[contains(@class,'search') and (self::input or self::div or self::button)]"
            + " | //*[contains(@placeholder,'Search') or contains(@placeholder,'search')]");

    private final By searchOverlay = By.xpath(
            "//div[contains(@class,'product-search-modal') or contains(@class,'search-modal') or contains(@class,'search-overlay')]"
                    + " | //div[contains(@role,'dialog') and .//input[contains(@placeholder,'Search') or contains(@placeholder,'search')]]"
                    + " | //div[contains(@class,'modal') and .//input[contains(@placeholder,'Search') or contains(@placeholder,'search')]]"
                    + " | //div[contains(@class,'oui-modal')]//input[contains(@placeholder,'Search') or contains(@placeholder,'search')]/ancestor::div[contains(@class,'oui-modal') or contains(@class,'modal') or contains(@class,'dialog') or contains(@class,'overlay')][1]"
                    + " | //input[contains(@placeholder,'Search') or contains(@placeholder,'search')]"
    );
    private final By searchInput = By.xpath("//div[contains(@class,'oui-input')]//input | //input[contains(@placeholder,'Search') or contains(@placeholder,'search')]");

    private final By allTab      = By.xpath("//*[self::button or self::div or self::span or @role='tab'][.//span[normalize-space()='All'] or normalize-space()='All'] | //button[contains(translate(.,'ALL','all'),'all')]");
    private final By testsTab    = By.xpath("//*[self::button or self::div or self::span or @role='tab'][.//span[normalize-space()='Tests'] or normalize-space()='Tests'] | //button[contains(translate(.,'TESTS','tests'),'tests')]");
    private final By checkupsTab = By.xpath("//*[self::button or self::div or self::span or @role='tab'][.//span[normalize-space()='Checkups'] or normalize-space()='Checkups'] | //button[contains(translate(.,'CHECKUPS','checkups'),'checkups')]");

    private final By addbutton = By.xpath(
            "(//div[contains(@class,'search-modal') or contains(@class,'product-search-modal')]//article[1]//button" +
            " | //div[contains(@class,'search-modal') or contains(@class,'product-search-modal')]//button[contains(@class,'add') or contains(translate(., 'ADD', 'add'), 'add') or contains(., '+')]" +
            " | //article[contains(@class,'modal--body')]//button[contains(@class,'add') or contains(translate(., 'ADD', 'add'), 'add') or contains(., '+')])[1]");

    private final By searchResults = By.xpath(
            "//div[contains(@class,'search-modal')]//article | //div[contains(@class,'product-search-modal')]//article | " +
            "//div[contains(@class,'search-modal')]//button[contains(., 'Add') or contains(., '+')]");

    private final By cartBadge = By.xpath(
            "//button[contains(@class,'cart')]//span[contains(@class,'icon') or contains(@class,'badge')] | //span[contains(@class,'cart-count')]");

    private final By proceedButton = By.xpath(
            "//button[contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//*[contains(@class, 'oui-button')][contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//div[@role='button'][contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//*[contains(@class, 'oui-button')]//*[contains(translate(., 'PROCEED', 'proceed'), 'proceed')]");

    private final By loginPage = By.xpath(
            "//div[contains(@class,'auth-primary-header')] | "
                    + "//*[contains(@class,'auth')] | "
                    + "//*[contains(@class,'login')] | "
                    + "//*[self::h1 or self::h2 or self::h3 or self::span or self::p or self::div][contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'sign in') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'login') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'),'mobile number')] | "
                    + "//button[contains(translate(., 'OTP', 'otp'),'otp')] | "
                    + "//input[contains(@type,'tel') or contains(@placeholder,'mobile') or contains(@placeholder,'Mobile') or contains(@name,'mobile')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /*=========================================================
     * Business Methods
     *=========================================================*/

    public String getPageTitle() {
        return getTitle();
    }

    public boolean isLogoDisplayed() {
        return isDisplayed(orangeHealthLogo);
    }

    public boolean isHomePageDisplayed() {
        waitForPageLoad();
        return isDisplayed(orangeHealthLogo, Duration.ofSeconds(30));
    }

    public void clickMenu(String menuName) {
        switch (menuName.trim().toLowerCase()) {
            case "my location":
                if (isLocationPopupDisplayed()) {
                    return;
                }
                try {
                    click(myLocationMenu);
                } catch (Exception e) {
                    jsClick(myLocationMenu);
                }
                try {
                    waitForVisibility(locationPopup, Duration.ofSeconds(10));
                } catch (Exception ignored) {
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown menu: " + menuName);
        }
    }

    public boolean isLocationPopupDisplayed() {
        return isDisplayed(locationPopup, Duration.ofSeconds(8));
    }

    public void selectCity(String city) {
        String lowerCity = city.trim().toLowerCase();
        String currentText = getSelectedCityText().toLowerCase();
        if (!currentText.isBlank() && (currentText.contains(lowerCity) || lowerCity.contains(currentText))) {
            return;
        }

        try {
            if (!isLocationPopupDisplayed()) {
                click(myLocationMenu);
            }
        } catch (Exception ignored) {
        }

        waitForPageLoad();
        String altCity = lowerCity.contains("bangalore") ? "bengaluru"
                : (lowerCity.contains("bengaluru") ? "bangalore" : lowerCity);

        By exact = cityButton(lowerCity, altCity);
        try {
            WebElement match = waitForClickable(exact, Duration.ofSeconds(5));
            scrollIntoView(match);
            match.click();
        } catch (Exception e) {
            try {
                jsClick(exact);
            } catch (Exception ex) {
                By fallback = By.xpath(String.format(
                        "//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]",
                        lowerCity, altCity));
                try {
                    jsClick(fallback);
                } catch (Exception ignored) {
                }
            }
        }
        closeLocationPopupIfStillOpen();
    }

    public boolean isSelectedCityDisplayed() {
        return !getSelectedCityText().isBlank();
    }

    public String getSelectedCityText() {
        return getText(selectedCity)
                .replaceAll("(?i)^MY LOCATION\\s*", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    public void clickSearchBar() {
        closeLocationPopupIfStillOpen();
        try {
            WebElement el = waitForClickable(searchBar, Duration.ofSeconds(10));
            scrollIntoView(el);
            el.click();
        } catch (Exception e) {
            jsClick(searchBar);
        }
        try {
            waitForVisibility(searchOverlay, Duration.ofSeconds(10));
        } catch (Exception ignored) {
        }
        try {
            waitForVisibility(allTab, Duration.ofSeconds(5));
        } catch (Exception ignored) {
        }
    }

    public boolean isSearchOverlayDisplayed() {
        if (isDisplayed(searchOverlay, PAGE_WAIT)) {
            return true;
        }
        return isDisplayed(searchInput, Duration.ofSeconds(5));
    }

    public boolean isSegmentedControlDisplayed(String control) {
        try {
            return waitForVisibility(segmentedControl(control), Duration.ofSeconds(5)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void navigateSegmentedControls() {
        click(allTab);
        click(testsTab);
        click(checkupsTab);
    }

    public boolean areSegmentedControlsClickable() {
        try {
            WebElement all      = waitForVisibility(allTab, FAST_CHECK);
            WebElement tests    = waitForVisibility(testsTab, FAST_CHECK);
            WebElement checkups = waitForVisibility(checkupsTab, FAST_CHECK);
            return all.isEnabled() && tests.isEnabled() && checkups.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void selectSegmentedControl(String control) {
        click(segmentedControl(control));
    }

    public void clickSearchInputField() {
        click(searchInput);
    }

    public void searchDiagnosticTest(String test) {
        WebElement input = waitForVisibility(searchInput, PAGE_WAIT);
        input.clear();
        input.sendKeys(test, Keys.ENTER);
        try {
            waitForVisibility(searchResults, PAGE_WAIT);
        } catch (Exception ignored) {
        }
    }

    public boolean isSearchResultDisplayed() {
        return isDisplayed(searchResults, Duration.ofSeconds(5));
    }

    public void addFirstResultToCart() {
        WebElement btn;
        try {
            btn = waitForClickable(addbutton, PAGE_WAIT);
        } catch (Exception e) {
            btn = waitForVisibility(addbutton, Duration.ofSeconds(10));
        }
        try {
            btn.click();
        } catch (Exception e) {
            jsClick(addbutton);
        }
        try {
            waitForVisibility(proceedButton, PAGE_WAIT);
        } catch (Exception ignored) {
            try {
                waitForVisibility(By.xpath("//button[contains(.,'Proceed')] | //div[contains(.,'Proceed')]"), PAGE_WAIT);
            } catch (Exception ignoredAgain) {
            }
        }
    }

    public int getCartBadgeCount() {
        try {
            String count = getAttribute(cartBadge, "data-count");
            if (count != null && !count.isBlank()) {
                return Integer.parseInt(count.trim());
            }
            String text = getText(cartBadge);
            if (text != null && !text.isBlank()) {
                String digits = text.replaceAll("[^0-9]", "").trim();
                if (!digits.isEmpty()) {
                    return Integer.parseInt(digits);
                }
            }
        } catch (Exception ignored) {
        }
        return 0;
    }

    public void clickProceedButton() {
        By proceedLocators = By.xpath(
            "//button[contains(translate(.,'PROCEED','proceed'),'proceed')] | " +
            "//button[contains(translate(.,'CHECKOUT','checkout'),'checkout')] | " +
            "//button[contains(translate(.,'VIEW CART','view cart'),'view cart')] | " +
            "//a[contains(translate(.,'VIEW CART','view cart'),'view cart') or contains(@href,'cart')] | " +
            "//*[contains(@class,'bottom-cart') or contains(@class,'footer')]//button | " +
            "//*[contains(@class,'bottom-cart') or contains(@class,'footer')]//a | " +
            "//div[contains(@class,'search-modal') or contains(@class,'product-search-modal')]//*[self::button or self::a or @role='button'][contains(translate(.,'PROCEED','proceed'),'proceed') or contains(translate(.,'VIEW CART','view cart'),'view cart') or contains(translate(.,'CART','cart'),'cart')] | " +
            "//div[contains(@class,'cart-modal')]//*[self::button or self::a or @role='button'][contains(translate(.,'PROCEED','proceed'),'proceed') or contains(translate(.,'CHECKOUT','checkout'),'checkout')]"
        );

        WebElement element = null;
        try {
            element = waitForFirstVisible(proceedLocators);
        } catch (Exception e1) {
            try {
                element = (WebElement) executeScript(
                        "return [...document.querySelectorAll('button, [class*=oui-button], a, [role=button], span, label, div')]"
                                + ".find(el => el.offsetWidth > 0 && el.offsetHeight > 0 && (el.textContent.trim().toLowerCase().includes('proceed') || el.textContent.trim().toLowerCase().includes('view cart') || el.textContent.trim().toLowerCase().includes('checkout')));");
            } catch (Exception ignored) {}
        }

        if (element != null) {
            scrollIntoView(element);
            try {
                element.click();
            } catch (Exception e) {
                executeScript("arguments[0].click();", element);
            }
        }

        try {
            wait(Duration.ofSeconds(5)).until(ExpectedConditions.or(
                ExpectedConditions.urlContains("auth"),
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.urlContains("checkout"),
                ExpectedConditions.visibilityOfElementLocated(loginPage)
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
        if (isDisplayed(loginPage, Duration.ofSeconds(10))) {
            return true;
        }
        try {
            return !driver.findElements(By.xpath("//input[@type='tel' or contains(@placeholder,'mobile') or contains(@placeholder,'Mobile') or contains(@name,'mobile')] | //button[contains(translate(.,'OTP','otp'),'otp')] | //*[contains(translate(.,'SIGN IN','sign in'),'sign in') or contains(translate(.,'LOGIN','login'),'login')]")).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    /*=========================================================
     * Private Helpers
     *=========================================================*/

    private By cityButton(String lowerCity, String altCity) {
        String xpath = String.format(
                "//*[contains(@role,'dialog') or contains(@class,'modal') or contains(@class,'oui-modal') or contains(.,'Select your city')]"
                        + "//*[self::span or self::button or self::p or self::div][translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s' or "
                        + "translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s']",
                lowerCity, altCity);
        return By.xpath(xpath);
    }

    private By segmentedControl(String control) {
        switch (control.trim().toLowerCase()) {
            case "all":      return allTab;
            case "tests":    return testsTab;
            case "checkups": return checkupsTab;
            default:
                throw new IllegalArgumentException("Unknown segmented control: " + control);
        }
    }

    private void closeLocationPopupIfStillOpen() {
        if (waitForPopupToClose(SHORT_WAIT)) {
            waitForOverlayToClose();
            return;
        }

        try {
            waitForClickable(locationPopupCloseButton, POPUP_CLOSE_WAIT).click();
        } catch (Exception e) {
            actions.sendKeys(Keys.ESCAPE).perform();
        }

        waitForInvisibility(locationPopup);
        waitForOverlayToClose();
    }

    private boolean waitForPopupToClose(Duration timeout) {
        try {
            return waitForInvisibility(locationPopup, timeout);
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void waitForOverlayToClose() {
        try {
            waitForInvisibility(locationOverlay, POPUP_CLOSE_WAIT);
        } catch (Exception e) {
        }
    }
}
