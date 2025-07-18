package persional.jobfinder_api.service;


import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.respones.JobResponse;

import java.util.List;

public interface JobService {

    JobResponse create(JobRequestDTO jobRequestDTO);
    JobResponse update(Long id, JobRequestDTO jobRequestDTO);
    JobResponse getById(Long id);
    List<JobResponse> getAll();
    void delete(Long id);
}