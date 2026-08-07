@HomePage
@Smoke
@Regression

Feature: Search and Book Diagnostic Test from Home Page

  As a patient
  I want to search and add a diagnostic test
  So that I can proceed with booking

  Background:
    Given the user launches the Orange Health application
    Then the Orange Health Home Page should be displayed

  @Smoke
  @Search
  @Booking

  Scenario: Verify user can search and proceed with a diagnostic test

    When the user clicks the "My Location" menu

    Then the Location Selection dialog should be displayed

    When the user selects the city from Excel

    Then the selected city should be displayed on the Home Page

    When the user clicks the Search Bar

    Then the Search Overlay should be displayed

    And the following segmented controls should be available
      | All |
      | Tests |
      | Checkups |

    When the user navigates through all segmented controls

    Then each segmented control should be clickable

    When the user selects the "All" segmented control

    And the user clicks the Search Input Field

    And the user searches for the diagnostic test from Excel

    Then the relevant search results should be displayed

    When the user adds the first search result to the cart

    Then the cart badge count should increase

    When the user clicks the Proceed button

    Then the Login page should be displayed