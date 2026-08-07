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

import com.orangehealth.base.BasePage;

public class HomePage extends BasePage {

    // Short wait for quick visibility checks
    private static final Duration SHORT_WAIT = Duration.ofSeconds(2);
    // Wait used when closing the location popup
    private static final Duration POPUP_CLOSE_WAIT = Duration.ofSeconds(3);
    // Fast check for segmented control availability
    private static final Duration FAST_CHECK = Duration.ofSeconds(2);

    /*=========================================================
     * Locators
     *=========================================================*/

    // Orange Health logo in the header
    private final By orangeHealthLogo = By
            .xpath("//header//img[@alt='Orange Health'] | //img[contains(@alt,'Orange Health')]");

    // Location picker button in the navigation bar
    private final By myLocationMenu = By.cssSelector("button.location-picker-button");

    // Same button used to read the currently selected city text
    private final By selectedCity = By.cssSelector("button.location-picker-button");

    // Location selection dialog/modal
    private final By locationPopup = By.xpath(
            "//div[contains(@role,'dialog') or contains(@class,'city-selector-modal') or contains(@class,'modal-body') or contains(@class,'oui-modal') or .//*[contains(text(),'Select your city')]]");

    // Close button inside the location popup
    private final By locationPopupCloseButton = By.xpath(
            "//*[contains(@role,'dialog') or contains(@class,'modal')]//button[contains(translate(@aria-label,'CLOSE','close'),'close') or contains(translate(@class,'CLOSE','close'),'close')]");

    // Semi-transparent overlay behind the location modal
    private final By locationOverlay = By.cssSelector("div.oui-modal--overlay");

    // Fake search input that opens the search overlay on click
    private final By searchBar = By.xpath(
            "//section[contains(@class,'search-section')]//div[contains(@class,'fake-search-input')] | //article[contains(@class,'search-container')]//div[contains(@class,'fake-search-input')]");

    // Full-screen search overlay modal
    private final By searchOverlay = By
            .xpath("//div[contains(@class,'product-search-modal') or contains(@class,'search-modal')]");

    // Actual text input field inside the search overlay
    private final By searchInput = By
            .xpath("//div[contains(@class,'oui-input')]//input | //input[contains(@placeholder,'Search')]");

    // Segmented control tabs inside the search overlay
    private final By allTab      = By.xpath("//button[.//span[normalize-space()='All'] or normalize-space()='All']");
    private final By testsTab    = By.xpath("//button[.//span[normalize-space()='Tests'] or normalize-space()='Tests']");
    private final By checkupsTab = By.xpath("//button[.//span[normalize-space()='Checkups'] or normalize-space()='Checkups']");

    // Add button on the first search result card (targets the button element, not its inner span)
    private final By addbutton = By.xpath(
            "(//article[contains(@class,'modal--body')]//article[1]//button[.//*[contains(text(),'Add') or contains(text(),'+')]] | " +
            "//div[contains(@class,'search-modal')]//article[1]//button[.//*[contains(text(),'Add') or contains(text(),'+')]])[1]");

    // Search result articles/cards inside the search modal
    private final By searchResults = By.xpath(
            "//div[contains(@class,'search-modal')]//article | //div[contains(@class,'product-search-modal')]//article | " +
            "//div[contains(@class,'search-modal')]//button[contains(., 'Add') or contains(., '+')]");

    // Cart icon badge showing the number of items in the cart
    private final By cartBadge = By.xpath(
            "//button[contains(@class,'cart')]//span[contains(@class,'icon') or contains(@class,'badge')] | //span[contains(@class,'cart-count')]");

    // Proceed button inside the cart/search modal
    private final By proceedButton = By.xpath(
            "//button[contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//*[contains(@class, 'oui-button')][contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//div[@role='button'][contains(translate(., 'PROCEED', 'proceed'), 'proceed')] | "
                    + "//*[contains(@class, 'oui-button')]//*[contains(translate(., 'PROCEED', 'proceed'), 'proceed')]");

    // Login/Sign-in page indicator element
    private final By loginPage = By.xpath(
            "//span[contains(normalize-space(.),'Sign in') or contains(normalize-space(.),'Login')] | //h2[contains(.,'Sign')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /*=========================================================
     * Business Methods
     *=========================================================*/

    // Returns the browser page title
    public String getPageTitle() {
        return getTitle();
    }

    // Returns true if the Orange Health logo is visible
    public boolean isLogoDisplayed() {
        return isDisplayed(orangeHealthLogo);
    }

    // Waits for page load then checks if the logo is visible (up to 10s)
    public boolean isHomePageDisplayed() {
        waitForPageLoad();
        return isDisplayed(orangeHealthLogo, Duration.ofSeconds(10));
    }

    // Clicks the specified navigation menu item by name
    public void clickMenu(String menuName) {
        switch (menuName.trim().toLowerCase()) {
            case "my location":
                // Skip click if the popup is already open
                if (isLocationPopupDisplayed()) {
                    return;
                }
                try {
                    click(myLocationMenu);
                } catch (Exception e) {
                    // Fall back to JS click if standard click fails
                    jsClick(myLocationMenu);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown menu: " + menuName);
        }
    }

    // Returns true if the location selection popup is currently visible
    public boolean isLocationPopupDisplayed() {
        return isDisplayed(locationPopup, Duration.ofSeconds(1));
    }

    // Selects a city from the location popup; handles Bangalore/Bengaluru alias
    public void selectCity(String city) {
        waitForVisibility(locationPopup, Duration.ofSeconds(10));
        String lowerCity = city.trim().toLowerCase();
        // Handle common alias between Bangalore and Bengaluru
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
                // Broad fallback: search anywhere inside the modal for the city name
                By fallback = By.xpath(String.format(
                        "//div[contains(@class,'city-selector-modal') or contains(@class,'oui-modal')]//*[contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '%s')]",
                        lowerCity));
                jsClick(fallback);
            }
        }
        closeLocationPopupIfStillOpen();
    }

    // Returns true if a city name is shown in the location button
    public boolean isSelectedCityDisplayed() {
        return !getSelectedCityText().isBlank();
    }

    // Returns the selected city text, stripping the "MY LOCATION" prefix if present
    public String getSelectedCityText() {
        return getText(selectedCity)
                .replaceAll("(?i)^MY LOCATION\\s*", "")
                .replaceAll("\\s+", " ")
                .trim();
    }

    // Opens the search overlay by JS-clicking the fake search bar
    public void clickSearchBar() {
        jsClick(searchBar);
        // Wait for the overlay and the All tab to be ready before proceeding
        waitForVisibility(searchOverlay, Duration.ofSeconds(10));
        waitForVisibility(allTab, Duration.ofSeconds(10));
    }

    // Returns true if the search overlay modal is visible
    public boolean isSearchOverlayDisplayed() {
        return isDisplayed(searchOverlay);
    }

    // Returns true if the specified segmented control tab is visible
    public boolean isSegmentedControlDisplayed(String control) {
        try {
            return waitForVisibility(segmentedControl(control), Duration.ofSeconds(5)).isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Clicks through All, Tests, and Checkups segmented control tabs
    public void navigateSegmentedControls() {
        click(allTab);
        click(testsTab);
        click(checkupsTab);
    }

    // Returns true if all three segmented control tabs are enabled
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

    // Clicks the specified segmented control tab
    public void selectSegmentedControl(String control) {
        click(segmentedControl(control));
    }

    // Clicks the search input field inside the overlay
    public void clickSearchInputField() {
        click(searchInput);
    }

    // Types the diagnostic test name and submits; waits for results to appear
    public void searchDiagnosticTest(String test) {
        WebElement input = waitForVisibility(searchInput, Duration.ofSeconds(5));
        input.clear();
        input.sendKeys(test + Keys.ENTER);
        try {
            waitForVisibility(searchResults, Duration.ofSeconds(5));
        } catch (Exception ignored) {
            // Results may already be visible before the wait triggers
        }
    }

    // Returns true if at least one search result card is visible
    public boolean isSearchResultDisplayed() {
        return isDisplayed(searchResults, Duration.ofSeconds(5));
    }

    // Clicks the Add button on the first search result; waits for Proceed to appear
    public void addFirstResultToCart() {
        WebElement btn = waitForClickable(addbutton, Duration.ofSeconds(5));
        try {
            btn.click();
        } catch (Exception e) {
            // Fall back to JS click if the button is partially obscured
            jsClick(addbutton);
        }
        try {
            waitForVisibility(proceedButton, Duration.ofSeconds(5));
        } catch (Exception ignored) {
            // Proceed button may appear after a short delay
        }
    }

    // Returns the cart item count from the badge; returns 0 if unreadable
    public int getCartBadgeCount() {
        try {
            // Try reading from data-count attribute first
            String count = getAttribute(cartBadge, "data-count");
            if (count != null && !count.isBlank()) {
                return Integer.parseInt(count.trim());
            }
            // Fall back to reading visible text and extracting digits
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

    // Scrolls the modal and clicks the Proceed button; uses JS fallback if needed
    public void clickProceedButton() {
        // Scroll the modal container to bring the Proceed button into view
        try {
            WebElement modal = waitForVisibility(
                    By.xpath("//div[contains(@class,'product-search-modal') or contains(@class,'search-modal') or contains(@class,'oui-modal')]"),
                    Duration.ofSeconds(3));
            executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", modal);
        } catch (Exception ignored) {
        }

        WebElement element = null;
        try {
            element = waitForVisibility(proceedButton, Duration.ofSeconds(4));
        } catch (Exception e1) {
            try {
                // JS fallback: find any element whose text matches 'proceed'
                element = (WebElement) executeScript(
                        "return [...document.querySelectorAll('button, [class*=oui-button], a, [role=button], span, label')]"
                                + ".find(el => el.textContent.trim().toLowerCase() === 'proceed' || el.textContent.trim().toLowerCase().includes('proceed'));");
            } catch (Exception e3) {
                throw new TimeoutException("Proceed button not found by any strategy");
            }
        }

        if (element == null) {
            throw new TimeoutException("Proceed button element is null");
        }

        scrollIntoView(element);

        try {
            element.click();
        } catch (Exception e) {
            executeScript("arguments[0].click();", element);
        }
    }

    // Returns true if the Login/Sign-in page is displayed (up to 10s)
    public boolean isLoginPageDisplayed() {
        return isDisplayed(loginPage, Duration.ofSeconds(10));
    }

    /*=========================================================
     * Private Helpers
     *=========================================================*/

    // Builds an XPath locator for a city button inside the location modal
    private By cityButton(String lowerCity, String altCity) {
        String xpath = String.format(
                "//*[contains(@role,'dialog') or contains(@class,'modal') or contains(@class,'oui-modal') or contains(.,'Select your city')]"
                        + "//*[self::span or self::button or self::p or self::div][translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s' or "
                        + "translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz') = '%s']",
                lowerCity, altCity);
        return By.xpath(xpath);
    }

    // Returns the By locator for the given segmented control tab name
    private By segmentedControl(String control) {
        switch (control.trim().toLowerCase()) {
            case "all":      return allTab;
            case "tests":    return testsTab;
            case "checkups": return checkupsTab;
            default:
                throw new IllegalArgumentException("Unknown segmented control: " + control);
        }
    }

    // Closes the location popup if it is still open after city selection
    private void closeLocationPopupIfStillOpen() {
        // If popup closed on its own, just wait for the overlay to disappear
        if (waitForPopupToClose(SHORT_WAIT)) {
            waitForOverlayToClose();
            return;
        }

        // Try clicking the close button; fall back to ESC key
        try {
            waitForClickable(locationPopupCloseButton, POPUP_CLOSE_WAIT).click();
        } catch (Exception e) {
            actions.sendKeys(Keys.ESCAPE).perform();
        }

        waitForInvisibility(locationPopup);
        waitForOverlayToClose();
    }

    // Returns true if the location popup disappears within the given timeout
    private boolean waitForPopupToClose(Duration timeout) {
        try {
            return waitForInvisibility(locationPopup, timeout);
        } catch (TimeoutException e) {
            return false;
        }
    }

    // Waits for the modal overlay to disappear; ignores if already gone
    private void waitForOverlayToClose() {
        try {
            waitForInvisibility(locationOverlay, POPUP_CLOSE_WAIT);
        } catch (Exception e) {
            // Overlay may already be detached from the DOM by this point
        }
    }
}
