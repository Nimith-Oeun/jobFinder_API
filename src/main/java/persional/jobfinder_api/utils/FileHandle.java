package persional.jobfinder_api.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.repository.UploadFileRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileHandle {

    private final UploadFileRepository uploadFileRepository;
    private final List<String> FILE_EXTENSIONS = List.of("pdf");

    //check if the file is empty
    public void validateUploadFile(MultipartFile files){
        if(files.isEmpty()){
            log.info("No file uploaded");
            throw new BadRequestException("No file uploaded");
        }
    }

    //check file format
    public void validateFileFormat(MultipartFile files){

        var fileName = StringUtils.cleanPath(Objects.requireNonNull(files.getOriginalFilename()));
        var extension = FileExtencion.getExtension(fileName);

        if (!FILE_EXTENSIONS.contains(extension)) {
            log.warn("File Extencion is not allow to upload , please verify: {}", fileName);
            throw new InternalServerError("File Extencion is not allow to upload , please verify: " + fileName);
        }
    }
}