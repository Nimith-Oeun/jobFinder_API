package persional.jobfinder_api.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionResponeDTO {

    private String errorCode;
    private String statusCode;
    private String message;
    private LocalDateTime timestamp;
    private Object responeData;
}