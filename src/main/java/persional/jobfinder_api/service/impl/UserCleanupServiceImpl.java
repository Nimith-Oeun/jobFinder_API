package persional.jobfinder_api.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.UserCleanupService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCleanupServiceImpl implements UserCleanupService {

    private final UserProfileRepository userProfileRepository;

    @Transactional
    @Override
    public void cleanupExpiredUnverifiedUsers() {

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(3);

        List<UserProfile> userNonActive =
                userProfileRepository.findByEnabledAndCreatedAtBefore(false, cutoffDate);

       if (!userNonActive.isEmpty()){
           userProfileRepository.deleteAllInBatch(userNonActive);
       }

    }
}
