package persional.jobfinder_api.dto.respones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class JobResponse {

    private Integer id;

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