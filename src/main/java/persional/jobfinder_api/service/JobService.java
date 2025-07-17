package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.JobRequest;
import persional.jobfinder_api.dto.respones.JobRespone;

import java.util.List;

public interface JobService {

    JobRespone create(JobRequest jobRequest);
    JobRespone update(Long id, JobRequest jobRequest);
    JobRespone getById(Long id);
    List<JobRespone> getAll();
    void delete(Long id);
}