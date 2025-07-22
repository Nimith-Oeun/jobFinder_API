package persional.jobfinder_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.model.JobApply;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.UploadFileService;

@Mapper(componentModel = "spring" , uses = {JobService.class , UploadFileService.class})
public interface JobApplyMapper {

    @Mapping(target = "job", source = "jobId")
    JobApply mapToJobApply(JobApplyRequestDTO request);

    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "resume", source = "resume.id")
    JobApplyRespone mapToJobApplyRespone(JobApply jobApply);
}
