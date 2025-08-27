package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.Resume;

public interface ResumeRopository extends JpaRepository<Resume, Long> {



}
