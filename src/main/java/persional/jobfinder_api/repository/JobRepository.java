package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

}