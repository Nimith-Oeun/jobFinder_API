package persional.jobfinder_api.service;


import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.model.Job;

import java.util.List;
import java.util.Map;

public interface JobService {

    JobResponse create(JobRequestDTO jobRequestDTO);
    JobResponse update(Long id, JobRequestDTO jobRequestDTO);
    Job getById(Long id);
    List<Job> searchjob(Map<String,String> param);
    void delete(Long id);
}