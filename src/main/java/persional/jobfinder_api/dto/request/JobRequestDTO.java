package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class JobRequestDTO {

    @JsonProperty("job_title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("company_name")
    private String company;

    @JsonProperty("thumbnail")
    private String thumbnail;

    @JsonProperty("location")
    private String location;

    @JsonProperty("salary")
    private double salary;

    @JsonProperty("application_instr")
    private String appliationInstr;

    @JsonProperty("job_type")
    private String jobType;

    @JsonProperty("job_category")
    private UUID jobCategoryUuid;

    @JsonProperty("skills")
    private Set<Skill> skills;

    @JsonProperty("job_requirements")
    private List<JobRequirement> jobRequirements;
}