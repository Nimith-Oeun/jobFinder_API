package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.enums.FileType;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.UploadFile;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.UploadFileRepository;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.UploadFileService;
import persional.jobfinder_api.service.UserService;
import persional.jobfinder_api.utils.FileExtencion;
import persional.jobfinder_api.utils.FileHandle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;
    private final FileHandle fileHandle;

    private static final String FILE_UPLOAD_PATH = System.getProperty("user.dir") + "/upload/";

    @Override
    public UploadFile save(MultipartFile file) {
        fileHandle.validateUploadFile(file);
        fileHandle.validateFilePhotoFormat(file);

        // get current user
        UserProfile profile = userService.getCurrentUserProfile();

        UserProfile userProfileId = userProfileRepository.findById(profile.getId())
                .orElseThrow(() -> new InternalServerError("User not found with id: " + profile.getId()));


        try {
            UploadFile uploadFile = new UploadFile();

            var name = FilenameUtils.removeExtension(file.getOriginalFilename());
            var extensionName = FileExtencion.getExtension(file.getOriginalFilename());
            var fileName = name + "." + extensionName;

            File filePathTemp = new File(FILE_UPLOAD_PATH + fileName);
            file.transferTo(filePathTemp);

            uploadFile.setFileFomate(extensionName);
            uploadFile.setFileSize(file.getSize());
            uploadFile.setFileType(String.valueOf(FileType.PHOTO));
            uploadFile.setFileName(name);
            uploadFile.setPartUpload(FILE_UPLOAD_PATH + fileName);
            uploadFile.setProfile(userProfileId);

            return uploadFileRepository.save(uploadFile);

        } catch (IOException e) {

            log.error("Error while uploading file: {}", e.getMessage());
            throw new InternalServerError("Error while uploading file: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<Resource> getfile() {
        UserProfile currentUserProfile = userService.getCurrentUserProfile();

        // Get file for this profile
        UploadFile file = uploadFileRepository.findByProfileId(currentUserProfile.getId())
                .orElseThrow(() -> new RuntimeException("File not found"));

        // Use the stored absolute path (it includes the correct filename and extension)
        Path path = Paths.get(file.getPartUpload());

        try {
            if (!Files.exists(path)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Detect content type
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .contentLength(Files.size(path))
                    .body(resource);

        } catch (IOException e) {
            log.error("Error while reading file: {}", e.getMessage());
            throw new InternalServerError("Error while reading file: " + e.getMessage());
        }
    }
}
