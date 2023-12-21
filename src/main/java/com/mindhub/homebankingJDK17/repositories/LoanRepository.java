package com.mindhub.homebankingJDK17.repositories;

import com.mindhub.homebankingJDK17.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long> {

    boolean existsByName(String name);

    boolean existsById(long id);

    Loan findByName(String name);
}
