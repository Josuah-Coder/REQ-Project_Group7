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

  @Customer @MVP2
  Background:
    Given the system has the following locations:
      | Name                     | City     | Available AC | Available DC | AC Price | DC Price |
      | Shopping Center Berlin   | Berlin   | 4            | 2            | 0.45     | 0.55     |
      | Highway Rest Stop A8     | Augsburg | 2            | 6            | 0.50     | 0.60     |
      | Hotel Admiral Cologne    | Cologne  | 3            | 1            | 0.48     | 0.58     |

  Scenario: Display location details for Berlin shopping center
    When I as customer "max.mustermann@email.de" search for available charging points in "Berlin"
    And I select location "Shopping Center Berlin"
    Then I see the following location details:
      | Field                  | Value                                  |
      | Name                   | Shopping Center Berlin                 |
      | Address                | Alexanderplatz 1, 10178 Berlin         |
      | Operating Hours        | 24/7                                   |
      | Available AC Chargers  | 4 (Type 2, 22kW)                       |
      | Available DC Chargers  | 2 (CCS, 150kW)                         |
      | AC Price per kWh       | 0.45 €                                 |
      | DC Price per kWh       | 0.55 €                                 |
      | Parking Fees           | First 2 hours free                     |
      | Amenities              | Café, WiFi, Restrooms                  |
    And the total availability is 75%

  Scenario: Filter availabilities by charger type
    Given I search for charging points in "Augsburg"
    When I filter by "DC" charging points
    Then I see 6 available DC charging points
    And the charger types are:
      | Type | Connector | Power     | Availability |
      | DC   | CCS       | 150 kW    | 4 available   |
      | DC   | CCS       | 350 kW    | 2 available   |
    And the average DC price is 0.60 €/kWh