package com.mindhub.homebankingJDK17.services;

import com.mindhub.homebankingJDK17.dto.AccountDTO;
import com.mindhub.homebankingJDK17.models.Account;

import java.util.List;

public interface AccountService {

    Boolean existsAccountByNumber(String accountNumber);

    void saveAccount(Account account);

    List<Account> getAllAccounts();

    List<AccountDTO> getAllAccountDTO();

    Account getAccountById(Long id);

    AccountDTO getAccountDTOById(Long id);

    Account getAccountByNumber(String accountNumber);

    AccountDTO getAccountDTOByNumber(String accountNumber);

    void deleteAccountByNumber(String accountNumber);
}