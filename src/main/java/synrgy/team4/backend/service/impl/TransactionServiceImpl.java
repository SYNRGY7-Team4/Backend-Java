package synrgy.team4.backend.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.Transaction;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.repository.TransactionRepository;
import synrgy.team4.backend.service.TransactionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public BaseResponse<List<MutationResponse>> getMutations(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Transaction> transactions = transactionRepository.findByAccountFromAccountNumberOrAccountToAccountNumber(accountNumber, accountNumber);

        List<MutationResponse> mutationResponses = transactions.stream()
                .map(transaction -> new MutationResponse(
                        transaction.getId(),
                        transaction.getAccountFrom().getAccountNumber(),
                        transaction.getAccountTo().getAccountNumber(),
                        transaction.getAmount(),
                        transaction.getDatetime(),
                        transaction.getType(),
                        transaction.getStatus(),
                        transaction.getDescription()
                ))
                .collect(Collectors.toList());

        return BaseResponse.<List<MutationResponse>>builder()
                .success(true)
                .data(mutationResponses)
                .message("Mutations retrieved successfully.")
                .build();
    }

    @Override
    public Optional<Transaction> getMutationById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<Transaction> getTransactionById(UUID id) {
        return transactionRepository.findById(id);
    }
}
