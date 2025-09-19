package persional.jobfinder_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import persional.jobfinder_api.model.UploadFile;

import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {

    // get all files by profile id
    Optional<UploadFile> findByProfileId(Long id);

    // get the latest file by profile id
    Optional<UploadFile> findFirstByProfileIdOrderByIdDesc(Long profileId);
}
