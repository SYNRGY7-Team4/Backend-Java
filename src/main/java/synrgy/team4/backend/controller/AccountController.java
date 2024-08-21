package synrgy.team4.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import synrgy.team4.backend.model.dto.response.AccountResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponse> accountResponses = accounts.stream()
                .map(this::convertToAccountResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountResponses);
    }

    private AccountResponse convertToAccountResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .userId(account.getUser().getId())
                .userName(account.getUser().getName())
                .build();
    }
}
