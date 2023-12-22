package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.dto.TransactionDTO;
import com.mindhub.homebankingJDK17.models.Transaction;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAllTransactions();

    List<TransactionDTO> getAllTransactionDTO();

    Transaction getTransaction(Long id);

    TransactionDTO getTransactionDTO(Long id);

    void saveTransaction(Transaction transaction);

    void deleteTransactions(String accountNumber);
}
