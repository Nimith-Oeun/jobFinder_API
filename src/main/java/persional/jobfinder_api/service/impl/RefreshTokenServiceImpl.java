package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.model.RefreshTokenEntity;
import persional.jobfinder_api.repository.RefreshTokenRepository;
import persional.jobfinder_api.service.RefreshTokenService;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveActive(String email, String jti, Instant expiresAt) {
        RefreshTokenEntity e = new RefreshTokenEntity();
        e.setUserEmail(email);
        e.setJti(jti);
        e.setExpiresAt(expiresAt);
        refreshTokenRepository.save(e);
    }

    @Override
    public void verifyActiveOrThrow(String jti) {
        refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                .orElseThrow(() -> new RuntimeException("Refresh token revoked/invalid"));
    }

    @Override
    public void revoke(String jti) {

        RefreshTokenEntity e = refreshTokenRepository.findByJtiAndRevokedFalse(jti)
                .orElseThrow(() -> new RuntimeException("Refresh token already revoked/invalid"));
        e.setRevoked(true);
        e.setRevokedAt(Instant.now());
        refreshTokenRepository.save(e);

    }

}
