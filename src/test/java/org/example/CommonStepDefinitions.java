package org.example;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.And;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.example.model.*;
import org.example.manager.*;

public class CommonStepDefinitions {
    protected Customer currentCustomer;
    protected Operator currentOperator;
    protected Location currentLocation;
    protected ChargingPoint currentChargingPoint;
    protected List<Transaction> currentTransactions;
    protected Map<String, String> currentStats;

    @Given("I am logged in as an operator with appropriate permissions")
    public void i_am_logged_in_as_operator_with_permissions() {
        currentOperator = new Operator("admin@energy-sample.de", "Admin User");
    }

    @Given("I am logged in as operator {string}")
    public void i_am_logged_in_as_operator(String operatorEmail) {
        currentOperator = new Operator(operatorEmail, "Operator Name");
    }



    @Given("I have authenticated my account")
    public void i_have_authenticated_my_account() {
        assertNotNull(currentCustomer, "Customer should be set before authentication");
    }
}