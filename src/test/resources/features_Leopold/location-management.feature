Feature: Location Management
  As an operator
  I want to create and edit charging locations
  So that I can maintain an accurate and up-to-date charging network

  Scenario: Create new charging location
    Given I am logged in as an operator with appropriate permissions
    When I create a new charging location with all required details
    Then the location should be saved in the system
    And the location should become visible in the location overview
    And I should be able to add charging points to this location

  Scenario: Edit existing charging location
    Given I have an existing charging location in the system
    When I modify the location details (address, description, etc.)
    Then the changes should be permanently saved
    And the updated information should be immediately visible to customers