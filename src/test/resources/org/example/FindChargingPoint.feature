Feature: Find Charging Point
  As a customer
  I want to view charging locations and their prices
  So that I can make an informed decision about where to charge my vehicle

  Scenario: View all charging locations
    Given I am using the charging application
    When I open the location overview
    Then I should see a list of all available charging locations
    And each location should show basic information like address and availability

  Scenario: View prices per specific location
    Given I am viewing the list of charging locations
    When I select a specific charging location
    Then I should see the detailed pricing information for that location
    And I should understand the cost structure before starting a session