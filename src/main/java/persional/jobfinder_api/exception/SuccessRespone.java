package persional.jobfinder_api.exception;

import java.time.LocalDateTime;

public class SuccessRespone {

    public static   ExceptionResponeDTO buildResponse(String errorCode , String statusCode ,
                                                     String message , Object responeData){
        return ExceptionResponeDTO.builder()
                .errorCode(errorCode)
                .statusCode(statusCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .responeData(responeData)
                .build();
    }

    public static ExceptionResponeDTO success(Object object){
        return buildResponse(
                "200",
                "Ok",
                "Success",
                object
        );
    }
}
