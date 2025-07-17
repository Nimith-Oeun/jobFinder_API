package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.JobRequirement;

public interface JobRequirementRepository extends JpaRepository<JobRequirement, Long> {
}