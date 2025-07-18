package persional.jobfinder_api.service;

import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.model.JobCategory;

public interface JobCategoryService {

    JobCategory create(JobCategoryRequest jobCategoryRequest);
    JobCategory getById(Long id);
}
