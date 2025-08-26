package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobApplyRequestDTO {

    @JsonProperty("Job-Id")
    private Integer jobId;

    @JsonProperty("resume")
    private Integer resume;

    private Integer profileId;
}
