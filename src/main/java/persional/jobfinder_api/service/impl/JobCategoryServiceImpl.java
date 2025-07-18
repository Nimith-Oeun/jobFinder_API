package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.JobCategory;
import persional.jobfinder_api.repository.JobCataggoryRepository;
import persional.jobfinder_api.service.JobCategoryService;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCataggoryRepository jobCataggoryRepository;

    @Override
    public JobCategory create(JobCategoryRequest jobCategoryRequest) {

        if (jobCategoryRequest.getName().isEmpty()){
            throw new BadRequestException("Category name must not be empty");
        }

        if (jobCataggoryRepository.findByName(jobCategoryRequest.getName()).isPresent()){
            throw new InternalServerError("Skill already exists");
        }

        JobCategory jobCategory = new JobCategory();
        jobCategory.setName(jobCategoryRequest.getName());

        return jobCataggoryRepository.save(jobCategory);
    }

    @Override
    public JobCategory getById(Long id) {
        return jobCataggoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Job category not found with id: " + id));
    }
}
