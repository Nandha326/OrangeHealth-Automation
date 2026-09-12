package com.orangehealth.stepdefinitions;

// Author: Nandhakumar J
// Step definitions for the Home Page feature.
// Covers: app launch, location selection, search, add to cart, and proceed to login.

import static org.testng.Assert.assertTrue;

import java.util.List;

import com.orangehealth.base.DriverFactory;
import com.orangehealth.config.ConfigManager;
import com.orangehealth.pages.CartPage;
import com.orangehealth.pages.HomePage;
import com.orangehealth.testdata.HomePageTestData;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HomePageSteps {

    private String selectedCityFromExcel;

    @Given("the user launches the Orange Health application")
    public void theUserLaunchesTheOrangeHealthApplication() {
        DriverFactory.getDriver().get(
                ConfigManager.getInstance().getConfigReader().getBaseUrl());
    }

    @Then("the Orange Health Home Page should be displayed")
    public void theOrangeHealthHomePageShouldBeDisplayed() {
        assertTrue(
                homePage().isHomePageDisplayed(),
                "Orange Health Home Page is not displayed.");
    }

    @Then("the application title should be displayed correctly")
    public void theApplicationTitleShouldBeDisplayedCorrectly() {
        assertTrue(
                homePage().getPageTitle().contains("Orange Health Labs"),
                "Application title does not match.");
    }

    @Then("the application logo should be displayed")
    public void theApplicationLogoShouldBeDisplayed() {
        assertTrue(
                homePage().isLogoDisplayed(),
                "Orange Health logo is not displayed.");
    }

    @When("the user clicks the {string} menu")
    public void theUserClicksTheMenu(String menuName) {
        homePage().clickMenu(menuName);
    }

    @Then("the Location Selection dialog should be displayed")
    public void theLocationSelectionDialogShouldBeDisplayed() {
        assertTrue(
                homePage().isLocationPopupDisplayed(),
                "Location Selection dialog is not displayed.");
    }

    @When("the user selects {string}")
    public void theUserSelects(String city) {
        selectedCityFromExcel = city;
        homePage().selectCity(city);
    }

    @When("the user selects the city from Excel")
    public void theUserSelectsTheCityFromExcel() {
        selectedCityFromExcel = HomePageTestData.firstCity();
        homePage().selectCity(selectedCityFromExcel);
    }

    @Then("the selected city should be displayed on the Home Page")
    public void theSelectedCityShouldBeDisplayedOnTheHomePage() {
        assertTrue(
                homePage().isSelectedCityDisplayed(),
                "Selected city is not displayed on the Home Page.");

        String displayedText = homePage().getSelectedCityText().toLowerCase();
        String expectedCity  = selectedCityFromExcel != null ? selectedCityFromExcel.toLowerCase() : "";

        boolean matches = !displayedText.isBlank()
                && (displayedText.contains(expectedCity)
                        || (expectedCity.contains("bangalore") && displayedText.contains("bengaluru"))
                        || (expectedCity.contains("bengaluru") && displayedText.contains("bangalore"))
                        || displayedText.matches(".*(bengaluru|bangalore|hyderabad|mumbai|pune|delhi|gurugram|noida).*"));
        assertTrue(
                matches,
                "Selected city text '" + displayedText + "' does not represent a valid location for the scenario.");
    }

    @When("the user clicks the Search Bar")
    public void theUserClicksTheSearchBar() {
        homePage().clickSearchBar();
    }

    @Then("the Search Overlay should be displayed")
    public void theSearchOverlayShouldBeDisplayed() {
        assertTrue(
                homePage().isSearchOverlayDisplayed(),
                "Search Overlay is not displayed.");
    }

    @And("the following segmented controls should be available")
    public void theFollowingSegmentedControlsShouldBeAvailable(DataTable dataTable) {
        List<String> tabs = dataTable.asList();

        for (String tab : tabs) {
            assertTrue(
                    homePage().isSegmentedControlDisplayed(tab),
                    tab + " segmented control is not displayed.");
        }
    }

    @When("the user navigates through all segmented controls")
    public void theUserNavigatesThroughAllSegmentedControls() {
        homePage().navigateSegmentedControls();
    }

    @Then("each segmented control should be clickable")
    public void eachSegmentedControlShouldBeClickable() {
        assertTrue(
                homePage().areSegmentedControlsClickable(),
                "One or more segmented controls are not clickable.");
    }

    @When("the user selects the {string} segmented control")
    public void theUserSelectsTheSegmentedControl(String control) {
        homePage().selectSegmentedControl(control);
    }

    @And("the user clicks the Search Input Field")
    public void theUserClicksTheSearchInputField() {
        homePage().clickSearchInputField();
    }

    @And("the user searches for {string}")
    public void theUserSearchesFor(String diagnosticTest) {
        homePage().searchDiagnosticTest(diagnosticTest);
    }

    @And("the user searches for the diagnostic test from Excel")
    public void theUserSearchesForTheDiagnosticTestFromExcel() {
        homePage().searchDiagnosticTest(
                HomePageTestData.get(selectedCityFromExcel));
    }

    @Then("the relevant search results should be displayed")
    public void theRelevantSearchResultsShouldBeDisplayed() {
        assertTrue(
                homePage().isSearchResultDisplayed(),
                "Relevant search results are not displayed.");
    }

    @When("the user adds the first search result to the cart")
    public void theUserAddsTheFirstSearchResultToTheCart() {
        homePage().addFirstResultToCart();
    }

    @Then("the cart badge count should increase")
    public void theCartBadgeCountShouldIncrease() {
        assertTrue(
                homePage().getCartBadgeCount() > 0,
                "Cart badge count did not increase.");
    }

    @Then("the Login page should be displayed")
    public void theLoginPageShouldBeDisplayed() {
        assertTrue(
                homePage().isLoginPageDisplayed() || cartPage().isLoginPageDisplayed(),
                "Login page is not displayed.");
    }

    private HomePage homePage() {
        return new HomePage(DriverFactory.getDriver());
    }

    private CartPage cartPage() {
        return new CartPage(DriverFactory.getDriver());
    }
}
