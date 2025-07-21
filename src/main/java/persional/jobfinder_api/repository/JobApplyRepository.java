package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.JobApply;

import java.util.Optional;

public interface JobApplyRepository extends JpaRepository<JobApply, Long> {


}
