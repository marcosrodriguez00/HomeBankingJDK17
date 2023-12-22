package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.models.Client;
import com.mindhub.homebankingJDK17.models.ClientLoan;
import com.mindhub.homebankingJDK17.models.Loan;

public interface ClientLoanService {

    void saveClientLoan(ClientLoan clientLoan);

    ClientLoan getById(long id);

    boolean existsById(long id);

    boolean existsClientLoanByLoanAndClientAndIsActive(Loan loan, Client client, boolean isActive);
}