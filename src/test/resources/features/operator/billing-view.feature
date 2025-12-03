Feature: Billing Overview
  As an operator
  I want to view detailed billing information and charging sessions
  So that I can analyze revenue and customer usage patterns

  Scenario: View detailed invoice information
    Given I need to review financial data
    When I access the detailed billing section
    Then I should see comprehensive invoice details
    And I should be able to filter by customer, date range, and location

  Scenario: View all charging sessions
    Given I want to analyze charging activity
    When I view the charging sessions report
    Then I should see a complete overview of all charging sessions
    And I should be able to filter and sort by various criteria

  @Operator @MVP2

  Scenario: Display monthly statistics
    Given I select month "January 2024"
    When I open monthly statistics
    Then I see the following overview:
      | Metric                       | Value          |
      | Total Revenue                | 48,920.15 €    |
      | Total Transactions           | 1,245          |
      | Average per Transaction      | 39.29 €        |
      | Highest Single Transaction   | 189.50 €       |
      | Canceled Transactions        | 28 (2.3%)      |
      | Revenue by Location Top 3:   |                |
      | 1. Berlin-Alex               | 12,450.60 €    |
      | 2. Munich-Center             | 9,870.25 €     |
      | 3. Hamburg-Port              | 8,120.40 €     |
      | Peak Usage Time              | 18:00-19:00    |
      | Most Used Charger Type       | DC (65%)       |
    And I can view data as charts
    And I can compare to previous month (+8.2%)

  Scenario: Filter transaction history details
    Given I see transaction history for January 2024
    And there are 1,245 transactions
    When I apply the following filters:
      | Filter               | Value                    |
      | Location             | "Berlin-Alex"            |
      | Date                 | "15.01.2024 - 20.01.2024"|
      | Minimum Amount       | "50.00 €"                |
      | Charger Type         | "DC"                     |
      | Payment Status       | "Successful"             |
    Then I see 42 filtered transactions
    And the list shows:
      | Date        | Time     | Amount   | Duration | Energy  | Charging Point   |
      | 15.01.2024  | 18:30:15 | 67.50 €  | 45min    | 37.5 kWh| CP-BER-DC-02     |
      | 16.01.2024  | 19:15:22 | 89.20 €  | 55min    | 49.0 kWh| CP-BER-DC-01     |
      | 17.01.2024  | 17:45:10 | 52.80 €  | 40min    | 33.0 kWh| CP-BER-DC-03     |
    And I can sort by any column
    And I can export all filtered transactions as CSV
    And the total sum of filtered transactions is 3,245.80 €