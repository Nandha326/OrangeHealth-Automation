@Tests
@Smoke
@Regression

Feature: Book a Lab Test Package

  Background:
    Given the user launches the Orange Health application

  Scenario: Verify user can book a Lab Test package

    When the user clicks the Tests navigation menu

    Then the Tests page should be displayed

    When the user scrolls to the Tests section

    And the user clicks the View Details button of the first Test

    Then the Tests detail page should be displayed

    When the user clicks the Add to Cart button

    Then the Cart Drawer should be displayed

    And the selected Test should be displayed in the cart

    When the user clicks the Proceed button

    Then the Sign in to Continue page should be displayed
