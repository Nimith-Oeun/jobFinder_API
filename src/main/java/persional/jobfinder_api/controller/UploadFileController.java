package persional.jobfinder_api.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import persional.jobfinder_api.exception.CustomMaxUploadSizeExceededException;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.service.UploadFileService;

import java.util.Map;

@RestController
@RequestMapping("/jobfinder_api/v1/file")
@RequiredArgsConstructor
@Slf4j
public class UploadFileController {

    private final UploadFileService uploadFileService;


    @PreAuthorize( "hasAuthority('upload:write')")
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadProfile(@RequestParam("file") MultipartFile file) {

        try{

            log.info("Uploading profile photo" + file.getOriginalFilename());

            //handle file size
            if (file.getSize() > 1024 * 1024) { // 5 MB limit
                 throw new CustomMaxUploadSizeExceededException("File size exceeds the maximum limit of 5 MB");
            }

            uploadFileService.save(file);
            return ResponseEntity.ok(SuccessRespone.success("File uploaded successfully"));

        }catch (Exception e){

            log.info("Error occurred while uploading photo: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of(
                    "status",
                    HttpStatus.BAD_REQUEST,
                    "File to upload file",
                    e.getMessage()));

        }
    }

    @GetMapping("/getPhoto")
    public ResponseEntity<?> getFilePhoto() {
        return uploadFileService.getfile();
    }
 }