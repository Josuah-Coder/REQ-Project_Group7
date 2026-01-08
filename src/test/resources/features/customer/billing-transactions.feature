Feature: Billing and Transaction History
  Background:
    Given I am customer "CUST-2024-001"
    And I have the following transactions in January 2024:
      | Date       | Location               | Amount   | Type      |
      | 2024-01-05 | Shopping Center Berlin | 12.45 €  | AC Charging|
      | 2024-01-10 | Highway Rest Stop A8   | 28.56 €  | DC Charging|
      | 2024-01-15 | Hotel Admiral Cologne  | 18.90 €  | AC Charging|
      | 2024-01-20 | Highway Rest Stop A8   | 35.20 €  | DC Charging|
      | 2024-01-25 | Shopping Center Berlin | 15.75 €  | AC Charging|

  Scenario: View list of charging sessions
    Given I have completed charging sessions in my account
    When I access the transaction history
    Then I should see a chronological list of all my charging sessions
    And each session should show date, duration, location, and energy consumed

  Scenario: View simple invoice amount
    Given I want to check my current charges
    When I view the billing overview
    Then I should see a clear, simple total invoice amount
    And I should understand what period this amount covers

  Scenario: View account balance and credit
    Given I am logged into my customer account
    When I check my financial information
    Then I should see my current account balance
    And I should see my available credit
    And I should understand if I need to add more funds

  @Customer @MVP2 @Billing


  Scenario: Display monthly overview for January 2024
    When I view the monthly overview for "January 2024"
    Then I see the following summary:
      | Metric                     | Value      |
      | Number of Transactions     | 5          |
      | Total Expenses             | 110.86 €   |
      | Average per Charge         | 22.17 €    |
      | AC Charging Costs          | 47.10 €    |
      | DC Charging Costs          | 63.76 €    |
      | Most Used Location         | Highway Rest Stop A8 (2×) |
    And the bar chart shows expenses per week
    And the pie chart shows distribution by location

  Scenario: Filter transactions by various criteria
    Given I see my transaction list
    When I filter by the following criteria:
      | Filter               | Value                    |
      | Time Period          | 2024-01-10 to 2024-01-20 |
      | Location             | Highway Rest Stop A8     |
      | Charger Type         | DC                       |
      | Minimum Amount       | 20.00 €                  |
    And I sort by "Amount descending"
    Then I see 2 transactions:
      | Date       | Amount   | Duration | Energy   |
      | 2024-01-20 | 35.20 €  | 55 min    | 44.0 kWh |
      | 2024-01-10 | 28.56 €  | 45 min    | 37.5 kWh |
    And the total sum of filtered transactions is "63.76 €"

  @ErrorCase
  Scenario: Handle filtering with invalid date range
    Given I see my transaction list
    When I filter by the following criteria:
      | Filter      | Value                    |
      | Time Period | 2024-05-01 to 2024-01-01 |
    Then I should see an error message "End date cannot be before start date"

  @EdgeCase
  Scenario: View billing overview with zero transactions
    Given I am customer "CUST-NEW-001"
    And I have no transactions in my history
    When I view the billing overview
    Then I should see a total invoice amount of "0.00 €"
    And the summary should show "0" transactions