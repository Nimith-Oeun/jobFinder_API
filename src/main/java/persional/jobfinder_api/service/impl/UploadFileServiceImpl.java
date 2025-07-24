package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.enums.FileType;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.UploadFile;
import persional.jobfinder_api.repository.UploadFileRepository;
import persional.jobfinder_api.service.UploadFileService;
import persional.jobfinder_api.utils.FileExtencion;
import persional.jobfinder_api.utils.FileHandle;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadFileServiceImpl implements UploadFileService {

    private final UploadFileRepository uploadFileRepository;
    private final FileHandle fileHandle;

    private static final String FILE_UPLOAD_PATH = System.getProperty("user.dir") + "/upload/";

    @Override
    public UploadFile save(MultipartFile file) {
        fileHandle.validateUploadFile(file);
        fileHandle.validateFileFormat(file);

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

            return uploadFileRepository.save(uploadFile);

        } catch (IOException e) {

            log.error("Error while uploading file: {}", e.getMessage());
            throw new InternalServerError("Error while uploading file: " + e.getMessage());
        }
    }


    @Override
    public UploadFile getFile(Long id) {
        return uploadFileRepository.findById(id)
                .orElseThrow(() -> new InternalServerError("File not found with id: " + id));
    }
}
