package com.mindhub.homebankingJDK17.services.implement;

import com.mindhub.homebankingJDK17.dto.AccountDTO;
import com.mindhub.homebankingJDK17.models.Account;
import com.mindhub.homebankingJDK17.repositories.AccountRepository;
import com.mindhub.homebankingJDK17.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AccountImplement implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public Boolean existsAccountByNumber(String accountNumber) {
        return accountRepository.existsByNumber(accountNumber);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public List<AccountDTO> getAllAccountDTO() {
        return getAllAccounts().stream().map(AccountDTO::new).collect(toList());
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public AccountDTO getAccountDTOById(Long id) {
        return new AccountDTO(getAccountById(id));
    }

    @Override
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByNumber(accountNumber);
    }

    @Override
    public AccountDTO getAccountDTOByNumber(String accountNumber) {
        return new AccountDTO(getAccountByNumber(accountNumber));
    }

    @Override
    public void deleteAccountByNumber(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        account.setActive(false);
        saveAccount(account);
    }
}
