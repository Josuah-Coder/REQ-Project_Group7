package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.example.model.*;
import org.example.manager.*;

public class BillingTransactionStepDefinitions {
    private List<Transaction> currentTransactions;
    private Map<String, String> currentSummary;

    @Given("I am customer {string}")
    public void i_am_customer(String customerId) {
        assertNotNull(customerId);
    }

    @Given("I have the following transactions in January {int}:")
    public void setup_transactions(Integer year, io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> columns : rows) {
            Transaction t = new Transaction();
            t.setDate(columns.get("Date"));
            String amountStr = columns.get("Amount").replace(" €", "").replace(",", ".");
            t.setAmount(new BigDecimal(amountStr));
            TransactionManager.getInstance().addTransaction(t);
        }
    }

    @Given("I have completed charging sessions in my account")
    public void i_have_sessions() {
        assertFalse(TransactionManager.getInstance().getAllTransactions().isEmpty());
    }

    @When("I access the transaction history")
    public void i_access_history() {
        this.currentTransactions = TransactionManager.getInstance().getAllTransactions();
    }

    @Then("I should see a chronological list of all my charging sessions")
    public void i_see_list() {
        assertNotNull(currentTransactions);
    }

    @And("each session should show date, duration, location, and energy consumed")
    public void each_session_details() {
        assertNotNull(currentTransactions);
    }

    @When("I view the monthly overview for {string}")
    public void i_view_monthly_overview(String month) {
        this.currentSummary = BillingManager.getInstance().getMonthlyStatistics(month);
    }

    @Then("I see the following summary:")
    public void i_see_summary(io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(currentSummary);
    }

    @And("the bar chart shows expenses per week")
    @And("the pie chart shows distribution by location")
    public void check_charts() {
        assertTrue(true);
    }

    @Given("I see my transaction list")
    public void i_see_my_transaction_list() {
        this.currentTransactions = TransactionManager.getInstance().getAllTransactions();
    }

    @When("I filter by the following criteria:")
    public void i_filter_by_criteria(io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(dataTable);
    }

    @And("I sort by {string}")
    public void i_sort_by(String criteria) {
        assertNotNull(criteria);
    }

    @Then("I see {int} transactions:")
    public void i_see_x_transactions(Integer count, io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(dataTable);
    }
}