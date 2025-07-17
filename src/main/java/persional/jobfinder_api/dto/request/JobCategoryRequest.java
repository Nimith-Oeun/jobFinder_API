package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobCategoryRequest {

    @JsonProperty ("category_name")
    private String name;

}
