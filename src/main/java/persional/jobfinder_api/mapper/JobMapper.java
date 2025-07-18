package persional.jobfinder_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.respones.JobRequirementResponse;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.dto.respones.SkillRespone;

import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobCategory;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;

import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface JobMapper {

    @Mapping(target = "jobCategoryUuid", source = "jobCategory")
    JobResponse mapToJobResponse(Job job);

    @Mapping(target = "jobCategory", source = "jobCategoryUuid")
    Job mapToJob(JobRequestDTO jobRequestDTO);

//    Set<SkillRespone> mapToSkillResponse(Set<Skill> skills);

    SkillRespone mapToSkillResponse(Skill skill);

    JobRequirementResponse mapToJobRequirementResponse(JobRequirement req);

    default JobCategory map(String uuid) {
        if (uuid == null) return null;
        JobCategory cat = new JobCategory();
        cat.setUuid(UUID.fromString(uuid));
        return cat;
    }

    default String map(JobCategory category) {
        if (category == null) return null;
        return category.getUuid().toString();
    }
}