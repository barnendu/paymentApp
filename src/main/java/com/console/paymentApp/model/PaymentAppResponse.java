package com.console.paymentApp.model;
import com.console.paymentApp.entity.Account;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class PaymentAppResponse {

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private List<Account> accounts;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    private List<Transaction> transactions;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
}
