package persional.jobfinder_api.dto.respones;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class JobResponse {

    private String title;

    private String description;

    private String company;

    private String thumbnail;

    private String location;

    private double salary;

    private String appliationInstr;

    private String jobType;

    private UUID jobCategoryUuid;

    private Set<SkillRespone> skills;

    private List<JobRequirementResponse> jobRequirements;

}