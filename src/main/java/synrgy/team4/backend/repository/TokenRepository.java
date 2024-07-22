package synrgy.team4.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import synrgy.team4.backend.model.entity.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {
    void deleteByJwtToken(String jwtToken);
    Optional<Token> findByRefreshToken(String refreshToken);
    void deleteByRefreshToken(String refreshToken);
}
