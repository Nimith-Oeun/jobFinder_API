package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.jwt.AuthUser;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserProfileRepository userProfileRepository;

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        UserProfile userProfile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new ResourNotFound("User not found with username: " + email));

        AuthUser authUser = AuthUser.builder()
                .username(userProfile.getUsername())
                .email(userProfile.getEmail())
                .password(userProfile.getPassword())
                .authorities(userProfile.getRole().getGrantedAuthorities())
                .accountNonExpired(userProfile.isAccountNonExpired())
                .accountNonLocked(userProfile.isAccountNonLocked())
                .credentialsNonExpired(userProfile.isCredentialsNonExpired())
                .enabled(userProfile.isEnabled())
                .build();

        return Optional.ofNullable( authUser);
    }

    @Override
    public UserProfile getUserProfileById(Long id) {
        return userProfileRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("User not found with id: " + id));
    }
}
