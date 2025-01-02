package org.serverapp.application.impl;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.serverapp.application.AccountService;
import org.serverapp.domain.entity.Account;
import org.serverapp.domain.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void save(Account account) {
        try {
            accountRepository.save(account);
        } catch (Exception e) {
            throw new RuntimeException("Error saving account: " + e.getMessage());
        }
    }
}
