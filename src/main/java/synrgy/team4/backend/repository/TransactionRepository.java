package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import synrgy.team4.backend.model.entity.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByAccountFromAccountNumberOrAccountToAccountNumber(String accountNumberFrom, String accountNumberTo);

    List<Transaction> findByDatetimeBetweenAndType(LocalDateTime datetime, LocalDateTime datetime2, String type);
}
