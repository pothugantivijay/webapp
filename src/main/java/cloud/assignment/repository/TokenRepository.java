package cloud.assignment.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import cloud.assignment.model.*;

public interface TokenRepository extends JpaRepository<TokenEntity, String> {
    Optional<TokenEntity> findByTokenAndVerifiedIsFalse(String token);
}
