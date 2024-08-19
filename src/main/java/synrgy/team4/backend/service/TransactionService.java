package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.entity.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    BaseResponse<List<MutationResponse>> getMutations(String accountNumber);

    BaseResponse<List<MutationResponse>> getMutationsByDate(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, String type);

    Optional<Transaction> getMutationById(UUID id);

    Optional<Transaction> getTransactionById(UUID id);

    Transaction makeTransaction(String accountFromNumber, String accountToNumber, BigDecimal amount, String description, String status, LocalDateTime dateTime);

    void processScheduledTransfers();
}
