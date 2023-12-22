package com.mindhub.homebankingJDK17.services.implement;

import com.mindhub.homebankingJDK17.models.Client;
import com.mindhub.homebankingJDK17.models.ClientLoan;
import com.mindhub.homebankingJDK17.models.Loan;
import com.mindhub.homebankingJDK17.repositories.ClientLoanRepository;
import com.mindhub.homebankingJDK17.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanImplement implements ClientLoanService {
    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Override
    public void saveClientLoan( ClientLoan clientLoan ) {
        clientLoanRepository.save( clientLoan );
    }

    @Override
    public ClientLoan getById(long id ) {
        return clientLoanRepository.findById( id );
    }

    @Override
    public boolean existsById( long id ) {
        return clientLoanRepository.existsById( id );
    }

    @Override
    public boolean existsClientLoanByLoanAndClientAndIsActive(Loan loan, Client client, boolean isActive) {
        return clientLoanRepository.existsByLoanAndClientAndIsActive(loan, client, isActive);
    }
}
