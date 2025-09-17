package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.ProfileUpdateRequest;
import persional.jobfinder_api.dto.respones.ProfileRespone;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.jwt.AuthUser;
import persional.jobfinder_api.mapper.ProfileMapper;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
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

    @Cacheable(value = "userProfiles" )
    @Override
    public ProfileRespone getCurrentUserProfile() {

        log.info("fetch Get current user profile");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        String email;
        if (principal instanceof UserDetails userDetails) {
            // Case 1: principal is UserDetails
            email = userDetails.getUsername();
        } else if (principal instanceof String) {
            // Case 2: principal is a String (your case now)
            email = (String) principal;
        } else {
            throw new RuntimeException("Unknown principal type: " + principal.getClass());
        }

        UserProfile userProfile = userProfileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User profile not found for email: " + email));

        String name = userProfile.getUsername();
        System.out.println("Name:" + name);

        return ProfileMapper.INSTANCE.mapToProfileRespone(userProfile);
    }

    @Override
    public UserProfile updateUser(ProfileUpdateRequest profileUpdateRequest) {

//        UserProfile currentUserProfile = getCurrentUserProfile();
//
//        UserProfile userProfile = userProfileRepository.findById(currentUserProfile.getId())
//                .orElseThrow(() -> new ResourNotFound("User not found with id: " + currentUserProfile.getId()));
//
//        userProfile.setUsername(profileUpdateRequest.getUsername());
//        userProfile.setFirstName(profileUpdateRequest.getFirstName());
//        userProfile.setLastName(profileUpdateRequest.getLastName());
//        userProfile.setAddress(profileUpdateRequest.getAddress());
//        userProfile.setGender(profileUpdateRequest.getGender());
//        userProfile.setBio(profileUpdateRequest.getBio());
//        userProfile.setDateOfBirth(profileUpdateRequest.getDateOfBirth());
//        userProfile.setFacebook(profileUpdateRequest.getFacebook());
//        userProfile.setInstagram(profileUpdateRequest.getInstagram());
//        userProfile.setLinkedin(profileUpdateRequest.getLinkedin());
//        userProfile.setPhoneNumber(profileUpdateRequest.getPhoneNumber());
//        userProfile.setTwitter(profileUpdateRequest.getTwitter());

//        return userProfileRepository.save(userProfile);
        return null;
    }


}
