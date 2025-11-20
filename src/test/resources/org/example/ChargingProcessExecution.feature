Feature: Charging Process Execution
As a customer
I want to start and stop charging sessions
So that I can efficiently charge my electric vehicle

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