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