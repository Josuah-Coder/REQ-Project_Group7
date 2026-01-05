package org.example;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.example.model.*;
import org.example.manager.*;

public class BillingStepDefinitions  {
    private List<Transaction> currentTransactions;
    private Map<String, String> currentStats;
    private Object currentOperator;
    private List<Invoice> invoices;
    private BigDecimal totalSum;

    @Given("I need to review financial data")
    public void i_need_to_review_financial_data() {
        currentOperator = OperatorManager.getInstance().getCurrentOperator();
    }

    @When("I access the detailed billing section")
    public void i_access_the_detailed_billing_section() {
        invoices = BillingManager.getInstance().getAllInvoices();
    }

    @Then("I should see comprehensive invoice details")
    public void i_should_see_comprehensive_invoice_details() {
        assertNotNull(invoices);
        assertFalse(invoices.isEmpty(), "Should see at least one invoice");
    }

    @And("I should be able to filter by customer, date range, and location")
    public void i_should_be_able_to_filter_by_customer_date_range_and_location() {
        assertDoesNotThrow(() -> {
            BillingManager.getInstance().filterInvoicesByCustomer("CUST-001");
            BillingManager.getInstance().filterInvoicesByDateRange("2024-01-01", "2024-01-31");
            BillingManager.getInstance().filterInvoicesByLocation("Berlin-Alex");
        });
    }

    @Given("I select month {string}")
    public void i_select_month(String month) {
        currentStats = BillingManager.getInstance().getMonthlyStatistics(month);
    }

    @When("I open monthly statistics")
    public void i_open_monthly_statistics() {
        assertNotNull(currentStats);
    }

    @Then("I see the following overview:")
    public void i_see_the_following_overview(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> expectedStats = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expectedStats.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();

            if (!expectedValue.isEmpty()) {
                String actualValue = currentStats.get(key);
                assertEquals(expectedValue, actualValue,
                        "Mismatch for metric: " + key);
            }
        }
    }

    @And("I can view data as charts")
    public void i_can_view_data_as_charts() {
        String chartData = BillingManager.getInstance().generateChartData(currentStats);
        assertNotNull(chartData);
        assertTrue(chartData.contains("chart") || chartData.contains("data"));
    }

    @And("I can compare to previous month {string}")
    public void i_can_compare_to_previous_month(String percentage) {
        String comparison = BillingManager.getInstance().getMonthComparison();
        assertTrue(comparison.contains(percentage),
                "Comparison should contain: " + percentage + " but was: " + comparison);
    }

    @Given("I see transaction history for {string}")
    public void i_see_transaction_history_for(String month) {
        currentTransactions = TransactionManager.getInstance().getTransactionsForMonth(month);
        assertNotNull(currentTransactions);
    }

    @Given("there are {int} transactions")
    public void there_are_transactions(int expectedCount) {
        assertEquals(expectedCount, currentTransactions.size(),
                "Expected " + expectedCount + " transactions");
    }

    @When("I apply the following filters:")
    public void i_apply_the_following_filters(io.cucumber.datatable.DataTable dataTable) {
        Map<String, String> filters = dataTable.asMap(String.class, String.class);

        currentTransactions = TransactionManager.getInstance().filterTransactions(
                filters.get("Location"),
                filters.get("Date"),
                new BigDecimal(filters.get("Minimum Amount").replace(" €", "")),
                filters.get("Charger Type"),
                filters.get("Payment Status")
        );
    }

    @Then("I see {int} filtered transactions")
    public void i_see_filtered_transactions(int expectedCount) {
        assertEquals(expectedCount, currentTransactions.size(),
                "Expected " + expectedCount + " filtered transactions");
    }

    @And("the list shows:")
    public void the_list_shows(io.cucumber.datatable.DataTable dataTable) {
        List<Map<String, String>> expectedRows = dataTable.asMaps();

        for (int i = 0; i < expectedRows.size(); i++) {
            Map<String, String> expected = expectedRows.get(i);
            Transaction actual = currentTransactions.get(i);

            assertEquals(expected.get("Date"), actual.getDate().toString());
            assertEquals(expected.get("Time"), actual.getTime().toString());
            assertEquals(expected.get("Amount"), actual.getAmount().toString() + " €");
            assertEquals(expected.get("Duration"), actual.getDuration());
            assertEquals(expected.get("Energy"), actual.getEnergy() + " kWh");
            assertEquals(expected.get("Charging Point"), actual.getChargingPointId());
        }
    }

    @And("I can sort by any column")
    public void i_can_sort_by_any_column() {
        List<Transaction> sortedByDate = TransactionManager.getInstance()
                .sortTransactions(currentTransactions, "date");
        List<Transaction> sortedByAmount = TransactionManager.getInstance()
                .sortTransactions(currentTransactions, "amount");

        assertNotNull(sortedByDate);
        assertNotNull(sortedByAmount);
    }

    @And("I can export all filtered transactions as CSV")
    public void i_can_export_all_filtered_transactions_as_csv() {
        String csv = TransactionManager.getInstance().exportToCSV(currentTransactions);
        assertNotNull(csv);
        assertTrue(csv.contains("Date,Time,Amount,Duration,Energy,Charging Point"),
                "CSV should contain headers");
    }

    @And("the total sum of filtered transactions is {string}")
    public void the_total_sum_of_filtered_transactions_is(String expectedTotal) {
        totalSum = currentTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expected = new BigDecimal(expectedTotal.replace(" €", "").replace(".", "").replace(",", "."));
        assertEquals(expected, totalSum,
                "Total sum should be " + expectedTotal);
    }
    @Given("I have the following transactions in January {int}:")
    public void i_have_the_following_transactions_in_january(Integer year, io.cucumber.datatable.DataTable dataTable) {

        System.out.println("Setting up test transactions for January " + year);


        List<Map<String, String>> transactions = dataTable.asMaps();
        System.out.println("Number of test transactions: " + transactions.size());

        for (Map<String, String> transaction : transactions) {
            System.out.println("Transaction: " + transaction);
        }


    }
}
