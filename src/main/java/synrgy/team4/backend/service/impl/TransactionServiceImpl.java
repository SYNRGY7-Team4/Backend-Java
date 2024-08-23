package synrgy.team4.backend.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import synrgy.team4.backend.model.dto.request.NotificationRequest;
import synrgy.team4.backend.model.dto.response.BaseResponse;
import synrgy.team4.backend.model.dto.response.MutationResponse;
import synrgy.team4.backend.model.entity.Account;
import synrgy.team4.backend.model.entity.Notification;
import synrgy.team4.backend.model.entity.Transaction;
import synrgy.team4.backend.model.entity.User;
import synrgy.team4.backend.repository.AccountRepository;
import synrgy.team4.backend.repository.NotificationRepository;
import synrgy.team4.backend.repository.TransactionRepository;
import synrgy.team4.backend.repository.UserRepository;
import synrgy.team4.backend.service.FCMService;
import synrgy.team4.backend.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private static final Logger log = LoggerFactory.getLogger(TransactionServiceImpl.class);

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository, UserRepository userRepository, NotificationRepository notificationRepository , FCMService fcmService) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
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
                        transaction.getAccountFrom().getUser().getName(),
                        transaction.getAccountTo().getAccountNumber(),
                        transaction.getAccountTo().getUser().getName(),
                        transaction.getAmount(),
                        transaction.getDatetime(),
                        transaction.getType(),
                        transaction.getStatus(),
                        transaction.getDescription(),
                        account.getBalance()
                ))
                .collect(Collectors.toList());

        return BaseResponse.<List<MutationResponse>>builder()
                .success(true)
                .data(mutationResponses)
                .message("Mutations retrieved successfully.")
                .build();
    }

    @Override
    public BaseResponse<List<MutationResponse>> getMutationsByDate(String accountNumber, LocalDateTime startDate, LocalDateTime endDate, String type) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        List<Transaction> transactions = transactionRepository.findByDatetimeBetweenAndType(startDate, endDate, type);

        List<MutationResponse> mutationResponses = transactions.stream()
                .map(transaction -> new MutationResponse(
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

    @Override
    @Transactional
    public Transaction makeTransaction(String accountFromNumber, String accountToNumber, BigDecimal amount, String description, String status, LocalDateTime dateTime) {
        Account accountFrom = accountRepository.findByAccountNumber(accountFromNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source account not found"));
        Account accountTo = accountRepository.findByAccountNumber(accountToNumber)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Destination account not found"));

        if (accountFrom.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient funds.");
        }

        if (status.equals("completed")) {
            accountFrom.setBalance(accountFrom.getBalance().subtract(amount));
            accountTo.setBalance(accountTo.getBalance().add(amount));

            accountRepository.save(accountFrom);
            accountRepository.save(accountTo);

            // Simpan notifikasi ke database
            User userTo = accountTo.getUser();
            if (userTo != null) {
                Notification notification = Notification.builder()
                        .user(userTo)
                        .title("Transfer Masuk")
                        .body("Anda menerima transfer sebesar Rp " + amount + " dari akun " + accountFromNumber)
                        .sentAt(LocalDateTime.now())
                        .build();
                notificationRepository.save(notification);

                // Cek dan kirim notifikasi menggunakan FCM jika token tersedia
                if (userTo.getFCMToken() != null) {
                    NotificationRequest notificationRequest = new NotificationRequest();
                    notificationRequest.setToken(userTo.getFCMToken());
                    notificationRequest.setTitle(notification.getTitle());
                    notificationRequest.setBody(notification.getBody());
                    notificationRequest.setTopic("transfer");

                    try {
                        fcmService.sendMessageToToken(notificationRequest);
                    } catch (InterruptedException | ExecutionException e) {
                        // Tangani error jika pengiriman notifikasi gagal
                        log.error("Failed to send notification to user: " + userTo);
                        e.printStackTrace();
                    }
                }
            }
        }

        Transaction transaction = Transaction.builder()
                .accountFrom(accountFrom)
                .accountTo(accountTo)
                .amount(amount)
                .datetime(dateTime != null ? dateTime : LocalDateTime.now().plusSeconds(10))
                .type("transfer")
                .status(status)
                .description(description)
                .build();
        log.info("Received datetime for transaction: {}", dateTime);

        return transactionRepository.save(transaction);
    }

    @Override
    @Scheduled(cron = "*/1 * * * * ?", zone = "Asia/Jakarta")
    public void processScheduledTransfers() {
        LocalDateTime now = LocalDateTime.now().withSecond(0).withNano(0);
        List<Transaction> scheduledTransactions = transactionRepository.findByStatusAndDatetimeLessThanEqual("pending", now);

        for (Transaction transaction : scheduledTransactions) {
            if (transaction.getDatetime().isBefore(now.plusSeconds(60))) {
                try {
                    Account accountFrom = transaction.getAccountFrom();
                    Account accountTo = transaction.getAccountTo();

                    if (accountFrom.getBalance().compareTo(transaction.getAmount()) < 0) {
                        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Insufficient funds.");
                    }

                    accountFrom.setBalance(accountFrom.getBalance().subtract(transaction.getAmount()));
                    accountTo.setBalance(accountTo.getBalance().add(transaction.getAmount()));

                    transaction.setStatus("completed");

                    accountRepository.save(accountFrom);
                    accountRepository.save(accountTo);
                    transactionRepository.save(transaction);

                    log.info("Transfer processed for account: {}", transaction.getAccountFrom().getAccountNumber());
                } catch (Exception e) {
                    transaction.setStatus("failed");
                    transactionRepository.save(transaction);
                    log.error("Error processing transfer for account: {}", transaction.getAccountFrom().getAccountNumber(), e);
                }
            }
        }
    }

}

