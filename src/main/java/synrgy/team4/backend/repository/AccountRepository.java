package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import synrgy.team4.backend.model.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    Account findByUserId(UUID userId);
}
