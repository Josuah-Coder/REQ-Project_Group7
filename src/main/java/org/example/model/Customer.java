package org.example.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String id;
    private String name;
    private String email;
    private BigDecimal accountBalance;
    private BigDecimal availableCredit;
    private List<Transaction> transactions;

    public Customer(String id, String name) {
        this.id = id;
        this.name = name;
        this.accountBalance = BigDecimal.ZERO;
        this.availableCredit = BigDecimal.valueOf(100.00);
        this.transactions = new ArrayList<>();
    }

    public Customer() {

    }

    public Customer withEmail(String email) {
        this.email = email;
        return this;
    }

    public Customer withAccountBalance(BigDecimal balance) {
        this.accountBalance = balance;
        return this;
    }

    public Customer withCredit(BigDecimal credit) {
        this.availableCredit = credit;
        return this;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public BigDecimal getAccountBalance() { return accountBalance; }
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getAvailableCredit() { return availableCredit; }
    public void setAvailableCredit(BigDecimal availableCredit) {
        this.availableCredit = availableCredit;
    }

    public List<Transaction> getTransactions() { return transactions; }
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public boolean hasSufficientFunds(BigDecimal amount) {
        return accountBalance.add(availableCredit).compareTo(amount) >= 0;
    }

    public void chargeAccount(BigDecimal amount) {
        if (accountBalance.compareTo(amount) >= 0) {
            accountBalance = accountBalance.subtract(amount);
        } else {
            BigDecimal remaining = amount.subtract(accountBalance);
            accountBalance = BigDecimal.ZERO;
            availableCredit = availableCredit.subtract(remaining);
        }
    }

    public void addFunds(BigDecimal amount) {
        accountBalance = accountBalance.add(amount);
    }

    @Override
    public String toString() {
        return String.format("Customer{id='%s', name='%s', balance=%.2f€, credit=%.2f€}",
                id, name, accountBalance, availableCredit);
    }
}
