package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import synrgy.team4.backend.model.entity.AccountList;

import java.util.List;
import java.util.UUID;

public interface AccountListRepository extends JpaRepository<AccountList, UUID> {
    List<AccountList> findByOwnerId(UUID ownerId);
}
