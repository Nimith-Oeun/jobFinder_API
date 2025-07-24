package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.UploadFile;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}
