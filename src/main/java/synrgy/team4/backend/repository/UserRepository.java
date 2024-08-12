package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import synrgy.team4.backend.model.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Boolean existsByNoKTP(String noKTP);
    Boolean existsByNoHP(String noHP);
    @Query("FROM User u WHERE LOWER(u.email) = LOWER(?1)")
    User findOneByUsername(String username);
    @Query("FROM User u WHERE u.otp = ?1")
    User findOneByOTP(String otp);
    @Query("FROM User u WHERE LOWER(u.email) = LOWER(:username)")
    User checkExistingEmail(String username);
}
