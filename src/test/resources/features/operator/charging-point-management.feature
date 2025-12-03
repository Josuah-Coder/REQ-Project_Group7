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

  @Operator @MVP2
  Scenario: Edit charging point configuration
    Given I see the details of charging point "CP-BER-DC-01"
    And current configuration is:
      | Setting          | Current Value        |
      | Max Power        | 150 kW               |
      | Connector Type   | CCS2                 |
      | Tariff Group     | Premium              |
      | Reservable       | No                   |
    When I make the following changes:
      | Setting          | New Value            |
      | Name             | "Fast Charger Berlin-01" |
      | Max Power        | 175 kW               |
      | Reservable       | Yes                  |
      | Reservation Fee  | 5.00 €               |
    Then the changes are saved
    And the charging point shows the new name
    And reservations are now possible

  Scenario: Change operational status
    Given charging point "CP-BER-AC-03" has status "AVAILABLE"
    And there are 2 active charging sessions
    When I change the operational status to "MAINTENANCE"
    And enter reason: "Annual safety inspection"
    And set time period: "22.01.2024 08:00 - 16:00"
    Then active users are notified
    And new charging sessions are blocked
    And a maintenance ticket is created (Ticket #WT-2024-015)
    And the dashboard status shows "Maintenance until 16:00"