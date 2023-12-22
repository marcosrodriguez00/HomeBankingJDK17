package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.dto.LoanDTO;
import com.mindhub.homebankingJDK17.models.Loan;

import java.util.List;

public interface LoanService {

    List<Loan> getAllLoans();

    List<LoanDTO> getAllLoanDTO();

    Loan getLoanById(Long id);

    LoanDTO getLoanDTOById(Long id);

    void saveLoan(Loan loan);

    boolean existsLoanByName(String name);

    void deleteLoanById(long id);

    boolean existsLoanById(long id);
}