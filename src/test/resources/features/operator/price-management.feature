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

  @Operator @MVP2
  Scenario: Copy prices between locations
    Given location "Highway Rest Stop A8" has pricing configuration:
      | Tariff     | Time Period    | Price/kWh | Base Fee |
      | Standard   | 06:00-22:00    | 0.60 €    | 1.50 €   |
      | Night      | 22:00-06:00    | 0.40 €    | 1.00 €   |
      | Weekend    | All day        | 0.55 €    | 1.50 €   |
    And I select "Copy Prices"
    And target location is "Shopping Center Berlin"
    When I increase prices by 15%
    And set start date to "01.02.2024"
    Then the target location has adjusted prices:
      | Tariff     | Time Period    | Price/kWh | Base Fee |
      | Standard   | 06:00-22:00    | 0.69 €    | 1.73 €   |
      | Night      | 22:00-06:00    | 0.46 €    | 1.15 €   |
      | Weekend    | All day        | 0.63 €    | 1.73 €   |
    And the change is documented in the log

  Scenario: Display change log
    Given I open the change log for location "Hotel Admiral Cologne"
    When I filter by period "January 2024"
    Then I see all price changes:
      | Date        | User            | Action                     | Old Price | New Price | Reason                 |
      | 05.01.2024  | price@energy.de | Prices increased by 5%     | 0.48 €    | 0.50 €    | Increased energy costs |
      | 12.01.2024  | admin@energy.de | Night tariff introduced    | -         | 0.40 €    | Boost demand           |
      | 20.01.2024  | manager@energy.de| Weekend discount          | 0.50 €    | 0.45 €    | Weekend promotion      |
    And I can revert any change
    And I see who authorized each change