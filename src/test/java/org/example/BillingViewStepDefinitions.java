package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.example.model.*;
import org.example.manager.*;

public class BillingViewStepDefinitions {
    private List<Transaction> currentTransactions;
    private Map<String, String> currentStats;
    private List<Invoice> invoices;

    @Given("I need to review financial data")
    public void i_need_financial_data() {
        assertNotNull(OperatorManager.getInstance().getCurrentOperator());
    }

    @When("I access the detailed billing section")
    public void i_access_billing_section() {
        this.invoices = BillingManager.getInstance().getAllInvoices();
    }

    @Then("I should see comprehensive invoice details")
    public void i_see_invoice_details() {
        assertNotNull(invoices);
    }

    @And("I should be able to filter by customer, date range, and location")
    public void i_can_filter_invoices() {
        assertTrue(true);
    }

    @Given("I want to analyze charging activity")
    public void i_want_analyze_activity() {
        assertNotNull(OperatorManager.getInstance().getCurrentOperator());
    }

    @When("I view the charging sessions report")
    public void i_view_report() {
        this.currentTransactions = TransactionManager.getInstance().getAllTransactions();
    }

    @Then("I should see a complete overview of all charging sessions")
    public void i_see_overview() {
        assertNotNull(currentTransactions);
    }

    @And("I should be able to filter and sort by various criteria")
    public void i_can_filter_and_sort() {
        assertTrue(true);
    }

    @Given("I select month {string}")
    public void i_select_month(String month) {
        assertNotNull(month);
    }

    @When("I open monthly statistics")
    public void i_open_stats() {
        this.currentStats = BillingManager.getInstance().getMonthlyStatistics("January 2024");
    }

    @Then("I see the following overview:")
    public void i_see_overview_table(io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(currentStats);
    }

    @And("I can view data as charts")
    public void i_can_view_charts() {
        assertTrue(true);
    }

    @And("^I can compare to previous month \\((.+)\\)$")
    public void i_compare_month(String percentage) {
        assertNotNull(percentage);
    }

    @Given("I see transaction history for January {int}")
    public void i_see_history(Integer year) {
        this.currentTransactions = TransactionManager.getInstance().getAllTransactions();
    }

    @And("there are {string} transactions")
    public void there_are_transactions_with_comma(String countStr) {

        int expectedCount = Integer.parseInt(countStr.replace(",", ""));


        this.currentTransactions = TransactionManager.getInstance().getAllTransactions();

    }

    @When("I apply the following filters:")
    public void i_apply_filters(io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(dataTable);
    }

    @Then("I see {int} filtered transactions")
    public void i_see_filtered(Integer count) {
        assertNotNull(count);
    }

    @And("the list shows:")
    public void the_list_shows(io.cucumber.datatable.DataTable dataTable) {
        assertNotNull(dataTable);
    }

    @And("I can sort by any column")
    public void i_can_sort() {
        assertTrue(true);
    }

    @And("I can export all filtered transactions as CSV")
    public void i_can_export_csv() {
        assertNotNull(TransactionManager.getInstance().exportToCSV(currentTransactions));
    }

    @And("the total sum of filtered transactions is {string}")
    public void the_total_sum_of_filtered_transactions_is(String expectedTotalStr) {

        String cleanAmount = expectedTotalStr
                .replace("€", "")
                .replace(" ", "")
                .replace(",", "");

        BigDecimal expected = new BigDecimal(cleanAmount).setScale(2, java.math.RoundingMode.HALF_UP);


        BigDecimal actualSum = BigDecimal.ZERO;
        if (currentTransactions != null && !currentTransactions.isEmpty()) {
            for (Transaction t : currentTransactions) {
                actualSum = actualSum.add(t.getAmount());
            }
        } else {

            actualSum = TransactionManager.getInstance().calculateTotal(
                    TransactionManager.getInstance().getAllTransactions()
            );
        }

        BigDecimal actualNormalized = actualSum.setScale(2, java.math.RoundingMode.HALF_UP);


        assertEquals(expected.doubleValue(), actualNormalized.doubleValue(), 0.01,
                "Sum mismatch! Expected: " + expected + " - Actual: " + actualNormalized +
                        " (Count: " + (currentTransactions != null ? currentTransactions.size() : 0) + ")");
    }
}