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

  @Operator @MVP2

  Scenario: Display location details
    Given I am logged in as operator "admin@energy-sample.de"
    And I select the location "Shopping Center Berlin" (ID: LOC-BER-001)
    When I open the location details
    Then I see the following information:
      | Field                | Value                                  |
      | Name                 | Shopping Center Berlin                 |
      | Address              | Alexanderplatz 1, 10178 Berlin         |
      | Operating Hours      | 24/7                                   |
      | Total Charging Points| 6 (4 AC, 2 DC)                         |
      | Operational Status   | Active                                 |
      | Created Date         | 15.03.2023                             |
      | Last Maintenance     | 10.01.2024                             |
      | Contact Person       | Max Mustermann (max@energy.de)         |
    And I can view all technical specifications