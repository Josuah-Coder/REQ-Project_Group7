Feature: Charging Point Management
  As an operator
  I want to add charging points to locations
  So that I can expand the charging capacity at existing locations

  Scenario: Add charging point to location
    Given I am managing an existing charging location
    When I add a new charging point with technical specifications
    Then the charging point should be created and associated with the location
    And the charging point should be available for customer use
    And the location capacity should be updated accordingly