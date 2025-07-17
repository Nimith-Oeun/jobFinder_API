package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.model.JobCatagory;

public interface JobCategoryService {

    JobCatagory create(JobCategoryRequest jobCategoryRequest);
    JobCatagory getById(Long id);
}
