package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.JobCategory;

import java.util.Optional;
import java.util.UUID;


public interface JobCataggoryRepository extends JpaRepository<JobCategory, Long> {

    Optional<JobCategory> findByUuid(UUID uuid);

    Optional<JobCategory> findByName(String name);
}