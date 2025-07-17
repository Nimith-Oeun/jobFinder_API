package persional.jobfinder_api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;

import java.util.List;
import java.util.Set;

@Data
public class JobRequest {

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

    @JsonProperty("application_instructions")
    private String appliationInstr;

    @JsonProperty("job_type")
    private String jobType;

    @JsonProperty("job_category")
    private String jobCategory;

    @JsonProperty("skills")
    private Set<Skill> skills;

    @JsonProperty("job_requirements")
    private List<JobRequirement> jobRequirements;
}