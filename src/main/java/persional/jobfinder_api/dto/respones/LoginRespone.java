package persional.jobfinder_api.dto.respones;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRespone {

    private String userName;
    private String token;
    private String refreshToken;
    private String[] authorities;

}
