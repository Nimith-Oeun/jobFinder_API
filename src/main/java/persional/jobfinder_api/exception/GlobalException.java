package persional.jobfinder_api.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.time.LocalDateTime;


@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponeDTO> handleBadRequestException(BadRequestException e){
        log.error("Bad request: {}", e.getMessage());
        ExceptionResponeDTO errorResponse = ExceptionResponeDTO.builder()
                .errorCode("400")
                .statusCode("BAD_REQUEST")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .responeData(new EmptyRespone())
                .build();
        return ResponseEntity.status(400).body(errorResponse);
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ExceptionResponeDTO> handleInternalServerError(InternalServerError e){
        log.error("Internal server error: {}", e.getMessage());
        ExceptionResponeDTO errorResponse = ExceptionResponeDTO.builder()
                .errorCode("500")
                .statusCode("INTERNAL_SERVER_ERROR")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .responeData(new EmptyRespone())
                .build();
        return ResponseEntity.status(500).body(errorResponse);
    }

    @ExceptionHandler(ResourNotFound.class)
    public ResponseEntity<ExceptionResponeDTO> handleResourceNotFoundException(ResourNotFound e){
        log.error("Resource not found: {}", e.getMessage());
        ExceptionResponeDTO errorResponse = ExceptionResponeDTO.builder()
                .errorCode("404")
                .statusCode("NOT_FOUND")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .responeData(new EmptyRespone())
                .build();
        return ResponseEntity.status(404).body(errorResponse);

    }

    @ExceptionHandler({CustomMaxUploadSizeExceededException.class, MaxUploadSizeExceededException.class})
    public ResponseEntity<ExceptionResponeDTO> handleLargeFileException(Exception e) {
        ExceptionResponeDTO errorResponse = ExceptionResponeDTO.builder()
                .errorCode("413")
                .statusCode("REQUEST_ENTITY_TOO_LARGE")
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .responeData(new EmptyRespone())
                .build();
        return ResponseEntity.status(413).body(errorResponse);
    }
    }
