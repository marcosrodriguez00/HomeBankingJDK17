package com.mindhub.homebankingJDK17.dto;

import com.mindhub.homebankingJDK17.models.Account;
import com.mindhub.homebankingJDK17.models.AccountType;
import com.mindhub.homebankingJDK17.models.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class AccountDTO {


    private long id;

    private String number;

    private double balance;

    private LocalDate creationDate;

    private List<TransactionDTO> transactions;

    private boolean isActive;

    private AccountType accountType;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.creationDate = account.getCreationDate();
        this.transactions = account.getTransactions().stream().filter(Transaction::isActive).map(TransactionDTO::new).collect(Collectors.toList());
        this.isActive = account.isActive();
        this.accountType = account.getAccountType();
    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public double getBalance() {
        return balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean isActive() {
        return isActive;
    }

    public AccountType getAccountType() {
        return accountType;
    }
}
