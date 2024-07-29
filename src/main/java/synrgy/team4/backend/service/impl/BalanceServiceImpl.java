package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.service.BalanceService;

import java.math.BigDecimal;

@Service
public class BalanceServiceImpl implements BalanceService {

    private final AccountRepository accountRepository;

    @Autowired
    public BalanceServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        return account.getBalance();
    }
}
