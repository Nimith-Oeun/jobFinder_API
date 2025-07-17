package persional.jobfinder_api.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import persional.jobfinder_api.dto.request.JobRequest;
import persional.jobfinder_api.dto.respones.JobCategoryRespone;
import persional.jobfinder_api.dto.respones.JobRequirementResponse;
import persional.jobfinder_api.dto.respones.JobRespone;
import persional.jobfinder_api.dto.respones.SkillRespone;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobCatagory;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")

public interface JobMapper {


    @Mapping(source = "jobCategory", target = "jobCatagory")
    Job mapToJob(JobRequest jobRequest);

    @Mapping(source = "jobCatagory", target = "jobCategory")
    JobRespone mapToJobRespone(Job job);

    Set<SkillRespone> mapToSkillResponse(Set<Skill> skills);

    JobRequirementResponse mapToJobRequirementResponse(JobRequirement req);

    default JobCatagory map(String uuid) {
        if (uuid == null) return null;
        JobCatagory cat = new JobCatagory();
        cat.setUuid(UUID.fromString(uuid)); // Set the UUID, not the ID
        return cat;
    }

    // JobCatagory → String (for Job → JobRespone)
    default String map(JobCatagory category) {
        if (category == null) return null;
        return category.getUuid().toString(); // or category.getName() if needed
    }

}