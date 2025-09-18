package persional.jobfinder_api.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.service.UserCleanupService;

@Service
@RequiredArgsConstructor
public class UserCleanupScheduler {


    private final UserCleanupService cleanupService;

    @Scheduled(cron = "0 0 2 * * ?") // runs every day at 2 AM
    public void runDailyCleanup() {
        cleanupService.cleanupExpiredUnverifiedUsers();
    }
}
