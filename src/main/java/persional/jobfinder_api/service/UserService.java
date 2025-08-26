package persional.jobfinder_api.service;

import persional.jobfinder_api.jwt.AuthUser;
import persional.jobfinder_api.model.UserProfile;

import java.util.Optional;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
    UserProfile getUserProfileById(Long id);
}
