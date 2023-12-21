package com.mindhub.homebankingJDK17.repositories;

import com.mindhub.homebankingJDK17.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByNumber(String number);

    Account findByNumber(String number);
}
