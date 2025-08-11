package persional.jobfinder_api.service;

import persional.jobfinder_api.jwt.AuthUser;

import java.util.Optional;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
}
