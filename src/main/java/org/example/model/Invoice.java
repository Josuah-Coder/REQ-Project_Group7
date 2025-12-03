package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private String id;
    private String customerId;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String period;
    private BigDecimal totalAmount;
    private BigDecimal vatAmount;
    private BigDecimal netAmount;
    private String status;
    private List<Transaction> transactions;

    public Invoice(String customerId, String period) {
        this.id = "INV-" + System.currentTimeMillis();
        this.customerId = customerId;
        this.period = period;
        this.invoiceDate = LocalDate.now();
        this.dueDate = invoiceDate.plusDays(30);
        this.totalAmount = BigDecimal.ZERO;
        this.vatAmount = BigDecimal.ZERO;
        this.netAmount = BigDecimal.ZERO;
        this.status = "Pending";
        this.transactions = new ArrayList<>();
    }

    public Invoice withTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.totalAmount = totalAmount.add(transaction.getAmount());
        return this;
    }

    public String getId() { return id; }

    public String getCustomerId() { return customerId; }

    public LocalDate getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public BigDecimal getVatAmount() { return vatAmount; }
    public void setVatAmount(BigDecimal vatAmount) { this.vatAmount = vatAmount; }

    public BigDecimal getNetAmount() { return netAmount; }
    public void setNetAmount(BigDecimal netAmount) { this.netAmount = netAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Transaction> getTransactions() { return transactions; }

    public void calculateTaxes() {
        this.netAmount = totalAmount.divide(BigDecimal.valueOf(1.20), 2, BigDecimal.ROUND_HALF_UP);
        this.vatAmount = totalAmount.subtract(netAmount);
    }

    @Override
    public String toString() {
        return String.format("Invoice{id='%s', customer='%s', period='%s', total=%.2f€, status='%s'}",
                id, customerId, period, totalAmount, status);
    }
}
