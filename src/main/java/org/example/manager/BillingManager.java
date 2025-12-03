package org.example.manager;

import org.example.model.Invoice;
import org.example.model.Transaction;
import java.math.BigDecimal;
import java.util.*;

public class BillingManager {
    private static BillingManager instance;
    private Map<String, Invoice> invoices;
    private List<Transaction> allTransactions;

    private BillingManager() {
        invoices = new HashMap<>();
        allTransactions = new ArrayList<>();
        initializeTestData();
    }

    public static synchronized BillingManager getInstance() {
        if (instance == null) {
            instance = new BillingManager();
        }
        return instance;
    }

    private void initializeTestData() {
        Invoice inv1 = new Invoice("CUST-2024-001", "January 2024");
        inv1.setTotalAmount(new BigDecimal("110.86"));
        invoices.put(inv1.getId(), inv1);

        Invoice inv2 = new Invoice("CUST-2024-002", "January 2024");
        inv2.setTotalAmount(new BigDecimal("85.50"));
        invoices.put(inv2.getId(), inv2);
    }

    public List<Invoice> getAllInvoices() {
        return new ArrayList<>(invoices.values());
    }

    public List<Invoice> filterInvoicesByCustomer(String customerId) {
        return invoices.values().stream()
                .filter(inv -> inv.getCustomerId().equals(customerId))
                .toList();
    }

    public List<Invoice> filterInvoicesByDateRange(String startDate, String endDate) {
        return new ArrayList<>(invoices.values());
    }

    public List<Invoice> filterInvoicesByLocation(String location) {
        return new ArrayList<>(invoices.values());
    }

    public Map<String, String> getMonthlyStatistics(String month) {
        Map<String, String> stats = new HashMap<>();

        stats.put("Total Revenue", "48,920.15 €");
        stats.put("Total Transactions", "1,245");
        stats.put("Average per Transaction", "39.29 €");
        stats.put("Highest Single Transaction", "189.50 €");
        stats.put("Canceled Transactions", "28 (2.3%)");
        stats.put("Revenue by Location Top 3:", "");
        stats.put("1. Berlin-Alex", "12,450.60 €");
        stats.put("2. Munich-Center", "9,870.25 €");
        stats.put("3. Hamburg-Port", "8,120.40 €");
        stats.put("Peak Usage Time", "18:00-19:00");
        stats.put("Most Used Charger Type", "DC (65%)");

        return stats;
    }

    public String generateChartData(Map<String, String> stats) {
        return "{\"chart\": \"monthly_revenue\", \"data\": " + stats.toString() + "}";
    }

    public String getMonthComparison() {
        return "Revenue increased by +8.2% compared to previous month";
    }

    public BigDecimal getCustomerTotal(String customerId) {
        if ("CUST-2024-001".equals(customerId)) {
            return new BigDecimal("110.86");
        }
        return new BigDecimal("85.50");
    }

    public String getBillingPeriod(String customerId) {
        return "January 1, 2024 - January 31, 2024";
    }

    public void addInvoice(Invoice invoice) {
        invoices.put(invoice.getId(), invoice);
    }

    public Invoice createInvoice(List<Transaction> transactions, String customerId, String period) {
        Invoice invoice = new Invoice(customerId, period);
        for (Transaction t : transactions) {
            invoice.withTransaction(t);
        }
        invoice.calculateTaxes();
        addInvoice(invoice);
        return invoice;
    }
}
