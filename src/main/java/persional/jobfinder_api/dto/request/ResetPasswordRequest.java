package persional.jobfinder_api.dto.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String email;
    private String newPassword;

}
