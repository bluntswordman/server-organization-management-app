package org.serverapp.application;

import org.serverapp.domain.entity.Account;

import java.util.Optional;

public interface AccountService {
    Optional<Account> findByEmail(String email);

    void save(Account account);
}
