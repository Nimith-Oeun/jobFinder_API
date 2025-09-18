package persional.jobfinder_api.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.model.UploadFile;

public interface UploadFileService {

    UploadFile save(MultipartFile file);
    ResponseEntity<Resource> getfile();
}
