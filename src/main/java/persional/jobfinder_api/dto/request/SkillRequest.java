package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SkillRequest {

    @JsonProperty ("skill_name")
    private String skillName;

}
