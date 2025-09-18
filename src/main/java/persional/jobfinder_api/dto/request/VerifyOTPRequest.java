package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VerifyOTPRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("otp_code")
    private String otp;

}
