package persional.jobfinder_api.service;

import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.model.UploadFile;

public interface UploadFileService {

    UploadFile save(MultipartFile file);
    UploadFile getFile(Long id);
}
