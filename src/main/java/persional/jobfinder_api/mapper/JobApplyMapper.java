package persional.jobfinder_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.model.JobApply;
import persional.jobfinder_api.model.Resume;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.ResumeService;
import persional.jobfinder_api.service.UploadFileService;
import persional.jobfinder_api.service.UserService;

@Mapper(componentModel = "spring" ,
        uses = {JobService.class ,
                UploadFileService.class ,
                UserService.class,
                ResumeService.class
        })
public interface JobApplyMapper {

    @Mapping(target = "job", source = "jobId")
    @Mapping(target = "profile", source = "profileId")
    @Mapping(target = "resume", source = "resumeId")
    JobApply mapToJobApply(JobApplyRequestDTO request);

    @Mapping(target = "jobId", source = "job.id")
    @Mapping(target = "resume", source = "resume.id")
    @Mapping(target = "profileId", source = "profile.id")
    JobApplyRespone mapToJobApplyRespone(JobApply jobApply);
}
