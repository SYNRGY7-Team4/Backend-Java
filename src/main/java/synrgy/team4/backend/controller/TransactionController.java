package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.request.TransferRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.dto.response.TransactionResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.Transaction;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.impl.TransactionServiceImpl;
import synrgy.team4.backend.service.impl.UserServiceImpl;
import synrgy.team4.backend.utils.PinHashing;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService, PinHashing pinHashing, UserServiceImpl userService) {
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

    @GetMapping("/mutation/date")
    public BaseResponse<List<MutationResponse>> getMutationDates(
            @RequestParam String accountNumber,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate,
            @RequestParam String type,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        BaseResponse<List<MutationResponse>> response = transactionService.getMutationsByDate(accountNumber, startDateTime, endDateTime, type);
        return ResponseEntity.ok(response).getBody();
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
                transaction.getAccountFrom().getUser().getName(),
                transaction.getAccountTo().getAccountNumber(),
                transaction.getAccountTo().getUser().getName(),
                transaction.getAmount(),
                transaction.getDatetime(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getDescription(),
                account.getBalance()
        );

        return BaseResponse.<MutationResponse>builder()
                .success(true)
                .data(mutationResponse)
                .message("Transaction retrieved successfully.")
                .build();
    }

    @PostMapping("/transfer")
    public BaseResponse<TransactionResponse> transfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(request.getAccountFrom()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        if (!PinHashing.verifyPin(request.getPin(), account.getPin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect pin.");
        }

        // Add the missing arguments
        Transaction transaction = transactionService.makeTransaction(request.getAccountFrom(), request.getAccountTo(), request.getAmount(), request.getDescription(), "completed", null);

        // Get the user names for both accounts involved in the transaction
        String userAccountFrom = transaction.getAccountFrom().getUser().getName();
        String userAccountTo = transaction.getAccountTo().getUser().getName();

        // Construct the response
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAccountFrom(transaction.getAccountFrom().getAccountNumber());
        transactionResponse.setNameAccountFrom(userAccountFrom);
        transactionResponse.setAccountTo(transaction.getAccountTo().getAccountNumber());
        transactionResponse.setNameAccountTo(userAccountTo);
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setDatetime(transaction.getDatetime());
        transactionResponse.setType(transaction.getType());
        transactionResponse.setStatus(transaction.getStatus());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setBalance(account.getBalance()); // Set balance

        return BaseResponse.<TransactionResponse>builder()
                .success(true)
                .data(transactionResponse)
                .message("Transfer successful.")
                .build();
    }

    @PostMapping("/schedule")
    public BaseResponse<TransactionResponse> scheduleTransfer(
            @RequestBody TransferRequest request,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<Account> accounts = userDetails.getAccounts();
        Account account = accounts.stream()
                .filter(acc -> acc.getAccountNumber().equals(request.getAccountFrom()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to access this account."));

        if (!PinHashing.verifyPin(request.getPin(), account.getPin())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Incorrect pin.");
        }

        // Ensure you pass all 6 arguments here
        Transaction transaction = transactionService.makeTransaction(request.getAccountFrom(), request.getAccountTo(), request.getAmount(), request.getDescription(), "pending", request.getDatetime());

        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setId(transaction.getId());
        transactionResponse.setAccountFrom(transaction.getAccountFrom().getAccountNumber());
        transactionResponse.setNameAccountFrom(transaction.getAccountFrom().getUser().getName());
        transactionResponse.setAccountTo(transaction.getAccountTo().getAccountNumber());
        transactionResponse.setNameAccountTo(transaction.getAccountTo().getUser().getName());
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setDatetime(transaction.getDatetime());
        transactionResponse.setType(transaction.getType());
        transactionResponse.setStatus(transaction.getStatus());
        transactionResponse.setDescription(transaction.getDescription());
        transactionResponse.setBalance(account.getBalance());

        return BaseResponse.<TransactionResponse>builder()
                .success(true)
                .data(transactionResponse)
                .message("Transfer scheduled successfully.")
                .build();
    }

}
