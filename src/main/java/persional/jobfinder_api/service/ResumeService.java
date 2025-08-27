package persional.jobfinder_api.service;

import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.model.Resume;

public interface ResumeService {

    Resume createResume(MultipartFile file);
}
