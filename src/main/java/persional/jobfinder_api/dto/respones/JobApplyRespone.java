package persional.jobfinder_api.dto.respones;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobApplyRespone {

    private Integer jobId;

    private Integer resume;
}
