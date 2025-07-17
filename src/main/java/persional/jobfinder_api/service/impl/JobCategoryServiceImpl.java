package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.model.JobCatagory;
import persional.jobfinder_api.model.Skill;
import persional.jobfinder_api.repository.JobCataggoryRepository;
import persional.jobfinder_api.repository.SkillRepository;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.SkillService;

@Service
@RequiredArgsConstructor
public class JobCategoryServiceImpl implements JobCategoryService {

    private final JobCataggoryRepository jobCataggoryRepository;

    @Override
    public JobCatagory create(JobCategoryRequest jobCategoryRequest) {

        if (jobCategoryRequest.getName().isEmpty()){
            throw new BadRequestException("Category name must not be empty");
        }

        if (jobCataggoryRepository.findByName(jobCategoryRequest.getName()).isPresent()){
            throw new InternalServerError("Skill already exists");
        }

        JobCatagory jobCatagory = new JobCatagory();
        jobCatagory.setName(jobCategoryRequest.getName());

        return jobCataggoryRepository.save(jobCatagory);
    }

    @Override
    public JobCatagory getById(Long id) {
        return jobCataggoryRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Job category not found with id: " + id));
    }
}
