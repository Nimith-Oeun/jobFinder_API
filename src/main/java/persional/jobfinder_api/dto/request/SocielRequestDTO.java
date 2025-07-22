package persional.jobfinder_api.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import persional.jobfinder_api.common.BaseEntity;

@Data
public class SocielRequestDTO extends BaseEntity {

    @JsonProperty("facebook")
    private String facebook;

    @JsonProperty("twitter")
    private String twitter;

    @JsonProperty("instagram")
    private String instagram;

    @JsonProperty("linkedIn")
    private String LinkedIn;

}