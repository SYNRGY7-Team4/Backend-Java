package synrgy.team4.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.response.AccountListResponse;
import synrgy.team4.backend.security.jwt.CustomUserDetails;
import synrgy.team4.backend.service.AccountListService;
import synrgy.team4.backend.model.dto.response.BaseResponse;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/account-list")
public class AccountListController {

    private final AccountListService accountListService;

    @Autowired
    public AccountListController(AccountListService accountListService) {
        this.accountListService = accountListService;
    }

    @PostMapping("/save")
    public BaseResponse<String> saveAccountToAccountList(
            @RequestBody Map<String, String> requestBody,
            Authentication authentication) {

        String accountNumber = requestBody.get("accountNumber");
        if (accountNumber == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number is required");
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        accountListService.saveAccountToAccountList(userDetails.getId(), accountNumber);

        return BaseResponse.<String>builder()
                .success(true)
                .message("Account saved successfully.")
                .build();
    }


    @GetMapping
    public BaseResponse<List<AccountListResponse>> getAccountList(Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<AccountListResponse> accountList = accountListService.getAccountList(userDetails.getId());

        return BaseResponse.<List<AccountListResponse>>builder()
                .success(true)
                .data(accountList)
                .message("Account list retrieved successfully.")
                .build();
    }
}
