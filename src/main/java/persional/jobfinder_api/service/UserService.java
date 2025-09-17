package persional.jobfinder_api.service;

import org.apache.catalina.User;
import persional.jobfinder_api.dto.request.ProfileUpdateRequest;
import persional.jobfinder_api.dto.respones.ProfileRespone;
import persional.jobfinder_api.jwt.AuthUser;
import persional.jobfinder_api.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
    UserProfile getUserProfileById(Long id);
    ProfileRespone getCurrentUserProfile();
    UserProfile updateUser(ProfileUpdateRequest profileUpdateRequest);
}
