package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.JobCatagory;

import java.util.Optional;
import java.util.UUID;


public interface JobCataggoryRepository extends JpaRepository<JobCatagory, Long> {

    Optional<JobCatagory> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    Optional<JobCatagory> findByName(String name);
}