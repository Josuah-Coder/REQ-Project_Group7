Feature: Charging Process Execution
  As a customer
  I want to start and stop charging sessions
  So that I can efficiently charge my electric vehicle

  Background:
    Given I am customer "CUST-2024-001" with account balance 150.00 €
    And I am at location "Highway Rest Stop A8"
    And DC charging point "CP-A8-03" is available with price 0.60 €/kWh

  Scenario: Start charging session
    Given I have selected an available charging point
    And I have authenticated my account
    When I initiate the charging start process
    Then the charging session should begin
    And the charging point status should change to "in use"
    And I should receive confirmation that charging has started

  Scenario: Stop charging session
    Given I have an active charging session
    When I initiate the charging stop process
    Then the charging session should end
    And the final charging data should be recorded
    And the charging point should become available for other users

  @Customer @MVP2 @Charging
  Scenario: Start charging session and bill successfully
    When I start charging at charging point "CP-A8-03"
    And I charge for 45 minutes at 150 kW
    Then the following session is recorded:
      | Field              | Value                         |
      | Session ID         | SESS-20240115-1830-001        |
      | Start Time         | 2024-01-15 18:30:00           |
      | Charging Point     | CP-A8-03 (DC, 150kW)          |
      | Estimated Cost     | 67.50 € (37.5 kWh × 0.60 €)   |
      | Current Status     | Active                        |
    And my account balance is 150.00 € beforehand
    And the session is monitored live

  Scenario: Pause and resume charging session
    Given I have an active charging session "SESS-20240115-1830-001"
    And 20 minutes have passed
    When I pause the charging session for 15 minutes
    And then resume
    Then the pause time of 15 minutes is not calculated
    And the effective charging time is 20 minutes
    And the total status shows "Paused for 00:15:00"