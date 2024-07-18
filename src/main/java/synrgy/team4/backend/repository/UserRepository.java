package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import synrgy.team4.backend.model.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);

}