package synrgy.team4.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.AccountList;
import synrgy.team4.backend.model.dto.response.AccountListResponse;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.AccountListRepository;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.service.AccountListService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountListServiceImpl implements AccountListService {

    private final AccountListRepository accountListRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountListServiceImpl(AccountListRepository accountListRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.accountListRepository = accountListRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveAccountToAccountList(UUID ownerId, String accountNumber) {
        Account savedAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Check if the account already exists in the user's account list
        boolean alreadyExists = accountListRepository.findByOwnerId(ownerId).stream()
                .anyMatch(accountList -> accountList.getSavedAccount().getId().equals(savedAccount.getId()));

        if (alreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Account is already in the account list");
        }

        AccountList accountList = AccountList.builder()
                .owner(owner)
                .savedAccount(savedAccount)
                .build();

        accountListRepository.save(accountList);
    }



    @Override
    public List<AccountListResponse> getAccountList(UUID ownerId) {
        List<AccountList> accountLists = accountListRepository.findByOwnerId(ownerId);

        return accountLists.stream()
                .map(accountList -> AccountListResponse.builder()
                        .id(accountList.getSavedAccount().getId())  // Include the ID in the response
                        .accountNumber(accountList.getSavedAccount().getAccountNumber())
                        .name(accountList.getSavedAccount().getUser().getName())
                        .build())
                .collect(Collectors.toList());
    }

}
