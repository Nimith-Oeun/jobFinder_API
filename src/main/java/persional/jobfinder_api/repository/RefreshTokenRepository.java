package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByJtiAndRevokedFalse(String jti);
}
