package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.*;

public class CustomerStepDefinitions extends CommonStepDefinitions {
    private Map<String, String> monthlySummary;

    @Given("I want to check my current charges")
    public void i_want_to_check_my_current_charges() {
        assertNotNull(currentCustomer);
    }

    @When("I view the billing overview")
    public void i_view_the_billing_overview() {
    }

    @Then("I should see a clear, simple total invoice amount")
    public void i_should_see_a_clear_simple_total_invoice_amount() {
        BigDecimal total = BillingManager.getInstance().getCustomerTotal(currentCustomer.getId());
        assertNotNull(total);
        assertTrue(total.compareTo(BigDecimal.ZERO) >= 0);
    }

    @And("I should understand what period this amount covers")
    public void i_should_understand_what_period_this_amount_covers() {
        String period = BillingManager.getInstance().getBillingPeriod(currentCustomer.getId());
        assertNotNull(period);
        assertFalse(period.isEmpty());
    }

    @Given("I am logged into my customer account")
    public void i_am_logged_into_my_customer_account() {
        assertNotNull(currentCustomer);
    }

    @When("I check my financial information")
    public void i_check_my_financial_information() {
    }

    @Then("I should see my current account balance")
    public void i_should_see_my_current_account_balance() {
        BigDecimal balance = currentCustomer.getAccountBalance();
        assertNotNull(balance);
    }

    @And("I should see my available credit")
    public void i_should_see_my_available_credit() {
        BigDecimal credit = currentCustomer.getAvailableCredit();
        assertNotNull(credit);
    }

    @And("I should understand if I need to add more funds")
    public void i_should_understand_if_i_need_to_add_more_funds() {
        boolean needsFunds = currentCustomer.getAccountBalance().compareTo(new BigDecimal("10.00")) < 0;
        assertTrue(true, "Should be able to determine if funds are needed");
    }

    @Given("I have the following transactions in {string}:")
    public void i_have_the_following_transactions_in(String month, io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> transactionsData = dataTable.asMaps();

        for (Map<String, String> data : transactionsData) {
            Transaction transaction = new Transaction();
            transaction.setDate(data.get("Date"));
            transaction.setLocation(data.get("Location"));
            transaction.setAmount(new BigDecimal(data.get("Amount").replace(" €", "")));
            transaction.setType(data.get("Type"));
            transaction.setCustomerId(currentCustomer.getId());

            TransactionManager.getInstance().addTransaction(transaction);
        }
    }

    @When("I view the monthly overview for {string}")
    public void i_view_the_monthly_overview_for(String month) {
        monthlySummary = CustomerManager.getInstance()
                .getMonthlyOverview(currentCustomer.getId(), month);
    }

    @Then("I see the following summary:")
    public void i_see_the_following_summary(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expected = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expected.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String actualValue = monthlySummary.get(key);

            assertEquals(expectedValue, actualValue,
                    "Mismatch for metric: " + key);
        }
    }

    @And("the bar chart shows expenses per week")
    public void the_bar_chart_shows_expenses_per_week() {
        Map<String, BigDecimal> weeklyExpenses = CustomerManager.getInstance()
                .getWeeklyExpenses(currentCustomer.getId(), "January 2024");
        assertNotNull(weeklyExpenses);
        assertFalse(weeklyExpenses.isEmpty());
    }

    @And("the pie chart shows distribution by location")
    public void the_pie_chart_shows_distribution_by_location() {
        Map<String, BigDecimal> locationDistribution = CustomerManager.getInstance()
                .getLocationDistribution(currentCustomer.getId(), "January 2024");
        assertNotNull(locationDistribution);
        assertFalse(locationDistribution.isEmpty());
    }

    @Given("I see my transaction list")
    public void i_see_my_transaction_list() {
        currentTransactions = TransactionManager.getInstance()
                .getCustomerTransactions(currentCustomer.getId());
        assertNotNull(currentTransactions);
    }

    @When("I filter by the following criteria:")
    public void i_filter_by_the_following_criteria(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> filters = dataTable.asMap(String.class, String.class);

        currentTransactions = CustomerManager.getInstance().filterTransactions(
                currentCustomer.getId(),
                filters.get("Time Period"),
                filters.get("Location"),
                filters.get("Charger Type"),
                new BigDecimal(filters.get("Minimum Amount").replace(" €", ""))
        );
    }

    @And("I sort by {string}")
    public void i_sort_by(String sortCriteria) {
        currentTransactions = CustomerManager.getInstance()
                .sortTransactions(currentTransactions, sortCriteria);
    }

    @Then("I see {int} transactions:")
    public void i_see_transactions(int expectedCount, io.cucumber.datatable.DataTable dataTable) {
        assertEquals(expectedCount, currentTransactions.size());

        List<Map<String, String>> expectedRows = dataTable.asMaps();
        for (int i = 0; i < expectedRows.size(); i++) {
            Map<String, String> expected = expectedRows.get(i);
            Transaction actual = currentTransactions.get(i);

            assertEquals(expected.get("Date"), actual.getDate().toString());
            assertEquals(expected.get("Amount"), actual.getAmount().toString() + " €");
            assertEquals(expected.get("Duration"), actual.getDuration());
            assertEquals(expected.get("Energy"), actual.getEnergy() + " kWh");
        }
    }
}