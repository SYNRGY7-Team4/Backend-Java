package synrgy.team4.backend.service;

import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.entity.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionService {
    BaseResponse<List<MutationResponse>> getMutations(String accountNumber);

    Optional<Transaction> getMutationById(UUID id);

    Optional<Transaction> getTransactionById(UUID id);
}
