Feature: Status Monitoring
  As an operator
  I want to monitor the status of charging points and view current prices
  So that I can ensure operational efficiency and proper pricing

  Scenario: View charging point status
    Given I am responsible for operational monitoring
    When I access the status dashboard
    Then I should see the current status of all charging points
    And I should be able to identify unavailable or faulty charging points

  Scenario: View current prices across locations
    Given I need to review the pricing strategy
    When I view the price overview
    Then I should see all current prices per location
    And I should be able to compare pricing across different locations