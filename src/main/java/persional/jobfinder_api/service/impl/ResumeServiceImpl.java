package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.dto.respones.ProfileRespone;
import persional.jobfinder_api.dto.respones.ResumeRespone;
import persional.jobfinder_api.enums.FileType;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.model.Resume;
import persional.jobfinder_api.model.UploadFile;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.ResumeRopository;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.ResumeService;
import persional.jobfinder_api.service.UserService;
import persional.jobfinder_api.utils.FileExtencion;
import persional.jobfinder_api.utils.FileHandle;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeServiceImpl implements ResumeService {
    
    private final ResumeRopository resumeRopository;
    private final FileHandle fileHandle;
    private final UserService userService;
    private final UserProfileRepository userProfileRepository;

    private static final String FILE_UPLOAD_PATH = System.getProperty("user.dir") + "/upload/";

    @CacheEvict(value = "resumes" , allEntries = true)
    @Override
    public Resume createResume(MultipartFile file) {

        fileHandle.validateUploadFile(file);
        fileHandle.validateFileCVFormat(file);

        // get current user
        ProfileRespone profile = userService.getCurrentUserProfile();

        UserProfile userProfileId = userProfileRepository.findById(profile.getId())
                .orElseThrow(() -> new InternalServerError("User not found with id: " + profile.getId()));


        try {
            
            Resume resume = new Resume();

            var name = FilenameUtils.removeExtension(file.getOriginalFilename());
            var extensionName = FileExtencion.getExtension(file.getOriginalFilename());
            var fileName = name + "." + extensionName;

            File filePathTemp = new File(FILE_UPLOAD_PATH + fileName);
            file.transferTo(filePathTemp);

            resume.setFileFomate(extensionName);
            resume.setFileSize(file.getSize());
            resume.setFileType(String.valueOf(FileType.PHOTO));
            resume.setFileName(name);
            resume.setPartUpload(FILE_UPLOAD_PATH + fileName);
            resume.setProfile(userProfileId);

            return resumeRopository.save(resume);

        } catch (IOException e) {

            log.error("Error while uploading file: {}", e.getMessage());
            throw new InternalServerError("Error while uploading file: " + e.getMessage());
        }

    }

    @Override
    public Resume getResumeById(Long id) {
        return resumeRopository.findById(id)
                .orElseThrow(() -> new InternalServerError("Resume not found with id: " + id));
    }

    @Cacheable(value = "resumes" )
    @Override
    public ResumeRespone getResumeByProfileId() {

        log.info("fetch Resume");

        // Get current user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        UserProfile profile = userProfileRepository.findByEmail(currentUserName)
                .orElseThrow(() -> new ResourNotFound("User not found"));

        // Get file for this profile
        Resume file = resumeRopository.findFirstByProfileIdOrderByIdDesc(profile.getId())
                .orElseThrow(() -> new ResourNotFound("File not found"));


        return ResumeRespone.builder()
                .id(file.getId())
                .fileType(file.getFileType())
                .fileFomate(file.getFileFomate())
                .fileSize(file.getFileSize())
                .fileName(file.getFileName())
                .build();
    }
}
