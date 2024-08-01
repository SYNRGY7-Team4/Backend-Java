package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.Transaction;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.TransactionService;
import synrgy.team4.backend.service.impl.TransactionServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/mutations")
    public BaseResponse<List<MutationResponse>> getMutations(@RequestParam String accountNumber, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        return transactionService.getMutations(accountNumber);
    }

    @GetMapping("/mutation")
    public BaseResponse<MutationResponse> getMutationById(@RequestParam UUID id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();

        Transaction transaction = transactionService.getTransactionById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found"));

        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(transaction.getAccountFrom().getAccountNumber()) ||
                        acc.getAccountNumber().equals(transaction.getAccountTo().getAccountNumber()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this transaction."));

        MutationResponse mutationResponse = new MutationResponse(
                transaction.getId(),
                transaction.getAccountFrom().getAccountNumber(),
                transaction.getAccountTo().getAccountNumber(),
                transaction.getAmount(),
                transaction.getDatetime(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription()
        );

        return BaseResponse.<MutationResponse>builder()
                .success(true)
                .data(mutationResponse)
                .message("Transaction retrieved successfully.")
                .build();
    }
}
