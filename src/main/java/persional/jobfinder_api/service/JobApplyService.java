package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.dto.respones.JobApplyResponeForClient;
import persional.jobfinder_api.model.JobApply;

import java.security.Principal;
import java.util.List;

public interface JobApplyService {

    JobApplyRespone createJobApply(JobApplyRequestDTO request);

//    JobApply getJobApplyById(Long id);

    List<JobApplyResponeForClient> getJobByCurrentUser(Principal principal);


}
