package persional.jobfinder_api.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;


import java.time.LocalDateTime;
import java.util.Map;


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
                .timestamp(LocalDateTime.now())
                .errorCode("413")
                .statusCode("REQUEST_ENTITY_TOO_LARGE")
                .message(e.getMessage())
                .responeData(new EmptyRespone())
                .build();
        return ResponseEntity.status(413).body(errorResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 401,
                        "error", "Unauthorized",
                        "path", request.getRequestURI()
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", 403,
                        "path", request.getRequestURI(),
                        "error", "Forbidden"
                ));
    }
}
