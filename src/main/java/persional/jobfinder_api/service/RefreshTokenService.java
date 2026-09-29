package persional.jobfinder_api.service;

import java.time.Instant;

public interface RefreshTokenService {

    void saveActive(String email, String jti, Instant expiresAt);
    void verifyActiveOrThrow(String jti);
    void revoke(String jti);
}
