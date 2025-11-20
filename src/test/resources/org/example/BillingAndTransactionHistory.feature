Feature: Billing and Transaction History
As a customer
I want to view my charging history and billing information
So that I can track my expenses and manage my account

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