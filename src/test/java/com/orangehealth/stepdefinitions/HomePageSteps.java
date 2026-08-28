package com.orangehealth.stepdefinitions;

// Author: Nandhakumar J
// Step definitions for the Home Page feature.
// Covers: app launch, location selection, search, add to cart, and proceed to login.

import static org.testng.Assert.assertTrue;

import java.util.List;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.config.ConfigManager;
import com.orangehealth.pages.HomePage;
import com.orangehealth.testdata.HomePageTestData;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HomePageSteps {

    private HomePage homePage;
    // Stores the city selected during the scenario for later assertion
    private String selectedCityFromExcel;

    // Launches the application by navigating to the base URL from config.json
    @Given("the user launches the Orange Health application")
    public void theUserLaunchesTheOrangeHealthApplication() {
        DriverFactory.getDriver().get(
                ConfigManager.getInstance().getConfigReader().getBaseUrl());
    }

    // Asserts that the Orange Health logo is visible on the home page
    @Then("the Orange Health Home Page should be displayed")
    public void theOrangeHealthHomePageShouldBeDisplayed() {
        assertTrue(
                homePage().isHomePageDisplayed(),
                "Orange Health Home Page is not displayed.");
    }

    // Asserts that the browser title contains the expected application name
    @Then("the application title should be displayed correctly")
    public void theApplicationTitleShouldBeDisplayedCorrectly() {
        assertTrue(
                homePage().getPageTitle().contains("Orange Health Labs"),
                "Application title does not match.");
    }

    // Asserts that the Orange Health logo element is visible
    @Then("the application logo should be displayed")
    public void theApplicationLogoShouldBeDisplayed() {
        assertTrue(
                homePage().isLogoDisplayed(),
                "Orange Health logo is not displayed.");
    }

    // Clicks the navigation menu item identified by the given name
    @When("the user clicks the {string} menu")
    public void theUserClicksTheMenu(String menuName) {
        homePage().clickMenu(menuName);
    }

    // Asserts that the city selection dialog is visible
    @Then("the Location Selection dialog should be displayed")
    public void theLocationSelectionDialogShouldBeDisplayed() {
        assertTrue(
                homePage().isLocationPopupDisplayed(),
                "Location Selection dialog is not displayed.");
    }

    // Selects the city passed directly from the feature file
    @When("the user selects {string}")
    public void theUserSelects(String city) {
        selectedCityFromExcel = city;
        homePage().selectCity(city);
    }

    // Reads the first city from the Excel test data file and selects it
    @When("the user selects the city from Excel")
    public void theUserSelectsTheCityFromExcel() {
        selectedCityFromExcel = HomePageTestData.firstCity();
        homePage().selectCity(selectedCityFromExcel);
    }

    // Asserts that the selected city name appears in the location button
    @Then("the selected city should be displayed on the Home Page")
    public void theSelectedCityShouldBeDisplayedOnTheHomePage() {
        assertTrue(
                homePage().isSelectedCityDisplayed(),
                "Selected city is not displayed on the Home Page.");
        String displayedText = homePage().getSelectedCityText().toLowerCase();
        String expectedCity  = selectedCityFromExcel.toLowerCase();
        // Accept both Bangalore and Bengaluru as equivalent city names
        boolean matches = displayedText.contains(expectedCity)
                || (expectedCity.contains("bangalore") && displayedText.contains("bengaluru"))
                || (expectedCity.contains("bengaluru") && displayedText.contains("bangalore"));
        assertTrue(
                matches,
                "Selected city text '" + displayedText + "' does not match expected '" + selectedCityFromExcel + "'.");
    }

    // Clicks the fake search bar to open the search overlay
    @When("the user clicks the Search Bar")
    public void theUserClicksTheSearchBar() {
        homePage().clickSearchBar();
    }

    // Asserts that the search overlay modal is visible
    @Then("the Search Overlay should be displayed")
    public void theSearchOverlayShouldBeDisplayed() {
        assertTrue(
                homePage().isSearchOverlayDisplayed(),
                "Search Overlay is not displayed.");
    }

    // Asserts that each tab listed in the DataTable is visible in the search overlay
    @And("the following segmented controls should be available")
    public void theFollowingSegmentedControlsShouldBeAvailable(DataTable dataTable) {
        List<String> tabs = dataTable.asList();

        for (String tab : tabs) {
            assertTrue(
                    homePage().isSegmentedControlDisplayed(tab),
                    tab + " segmented control is not displayed.");
        }
    }

    // Clicks through All, Tests, and Checkups segmented control tabs
    @When("the user navigates through all segmented controls")
    public void theUserNavigatesThroughAllSegmentedControls() {
        homePage().navigateSegmentedControls();
    }

    // Asserts that all three segmented control tabs are enabled
    @Then("each segmented control should be clickable")
    public void eachSegmentedControlShouldBeClickable() {
        assertTrue(
                homePage().areSegmentedControlsClickable(),
                "One or more segmented controls are not clickable.");
    }

    // Selects the specified segmented control tab by name
    @When("the user selects the {string} segmented control")
    public void theUserSelectsTheSegmentedControl(String control) {
        homePage().selectSegmentedControl(control);
    }

    // Clicks the text input field inside the search overlay
    @And("the user clicks the Search Input Field")
    public void theUserClicksTheSearchInputField() {
        homePage().clickSearchInputField();
    }

    // Types the diagnostic test name passed from the feature file
    @And("the user searches for {string}")
    public void theUserSearchesFor(String diagnosticTest) {
        homePage().searchDiagnosticTest(diagnosticTest);
    }

    // Reads the diagnostic test name from Excel (keyed by selected city) and searches
    @And("the user searches for the diagnostic test from Excel")
    public void theUserSearchesForTheDiagnosticTestFromExcel() {
        homePage().searchDiagnosticTest(
                HomePageTestData.get(selectedCityFromExcel));
    }

    // Asserts that at least one search result card is visible
    @Then("the relevant search results should be displayed")
    public void theRelevantSearchResultsShouldBeDisplayed() {
        assertTrue(
                homePage().isSearchResultDisplayed(),
                "Relevant search results are not displayed.");
    }

    // Clicks the Add button on the first search result card
    @When("the user adds the first search result to the cart")
    public void theUserAddsTheFirstSearchResultToTheCart() {
        homePage().addFirstResultToCart();
    }

    // Asserts that the cart badge count is greater than zero
    @Then("the cart badge count should increase")
    public void theCartBadgeCountShouldIncrease() {
        assertTrue(
                homePage().getCartBadgeCount() > 0,
                "Cart badge count did not increase.");
    }

    // Asserts that the Login/Sign-in page is displayed after clicking Proceed
    @Then("the Login page should be displayed")
    public void theLoginPageShouldBeDisplayed() {
        assertTrue(
                homePage().isLoginPageDisplayed(),
                "Login page is not displayed.");
    }

    // Lazy-initialises and returns the HomePage instance
    private HomePage homePage() {
        if (homePage == null) {
            homePage = new HomePage(DriverFactory.getDriver());
        }
        return homePage;
    }
}
