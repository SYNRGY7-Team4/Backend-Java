package synrgy.team4.backend.service;

import synrgy.team4.backend.model.entity.AccountList;
import synrgy.team4.backend.model.dto.response.AccountListResponse;

import java.util.List;
import java.util.UUID;

public interface AccountListService {
    void saveAccountToAccountList(UUID ownerId, String accountNumber);
    List<AccountListResponse> getAccountList(UUID ownerId);
}
