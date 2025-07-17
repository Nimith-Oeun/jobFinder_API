package persional.jobfinder_api.dto.respones;

import lombok.Data;


import java.util.List;
import java.util.Set;

@Data
public class JobRespone {

    private String title;

    private String description;

    private String company;

    private String thumbnail;

    private String location;

    private double salary;

    private String appliationInstr;

    private String jobType;

    private String jobCategory;

    private Set<SkillRespone> skills;

    private List<JobRequirementResponse> jobRequirements;

}