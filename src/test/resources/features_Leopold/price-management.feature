Feature: Price Management
  As an operator
  I want to set and modify prices for charging locations
  So that I can maintain competitive and profitable pricing strategies

  Scenario: Set prices for specific location
    Given I am managing a charging location
    When I define the pricing structure for this location
    Then the prices should be saved and associated with the location
    And customers should see these prices when considering this location

  Scenario: Modify existing prices
    Given I have existing prices set for a location
    When I update the pricing information
    Then the new prices should be saved
    And the changes should be applied to future charging sessions
    And active sessions should continue with the original prices