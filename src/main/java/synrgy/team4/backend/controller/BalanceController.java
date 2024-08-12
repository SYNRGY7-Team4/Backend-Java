package synrgy.team4.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.request.SetBalanceRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.BalanceService;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @Autowired
    public BalanceController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/get")
    public ResponseEntity<BaseResponse<BigDecimal>> getBalance(@RequestParam String accountNumber, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        BigDecimal balance = balanceService.getBalance(accountNumber);
        BaseResponse<BigDecimal> response = BaseResponse.<BigDecimal>builder()
                .success(true)
                .data(balance)
                .message("Balance retrieved successfully.")
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/set")
    public ResponseEntity<BaseResponse<String>> setBalance(@RequestBody SetBalanceRequest request, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(request.getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        balanceService.setBalance(request.getAccountNumber(), request.getNewBalance());

        BaseResponse<String> response = BaseResponse.<String>builder()
                .success(true)
                .message("Balance set successfully.")
                .data("The balance for account number " + request.getAccountNumber() + " has been set to " + request.getNewBalance())
                .build();

        log.info("Balance set successfully to {}", request.getNewBalance());

        return ResponseEntity.ok(response);
    }
}
