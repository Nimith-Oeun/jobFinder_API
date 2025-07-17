package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import persional.jobfinder_api.dto.request.JobRequest;
import persional.jobfinder_api.dto.respones.JobRespone;
import persional.jobfinder_api.dto.respones.SkillRespone;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.mapper.JobMapper;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobCatagory;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;
import persional.jobfinder_api.repository.JobCataggoryRepository;
import persional.jobfinder_api.repository.JobRepository;
import persional.jobfinder_api.repository.SkillRepository;
import persional.jobfinder_api.service.JobService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final JobCataggoryRepository jobCataggoryRepository;
    private final SkillRepository skillRepository;


    @Override
    public JobRespone create(JobRequest jobRequest) {

        // Validate job category UUID
        if (!StringUtils.hasText(jobRequest.getJobCategory())) {
            throw new BadRequestException("Job Category UUID must not be empty");
        }

        if (jobRequest.getSkills().isEmpty() || jobRequest.getJobRequirements().isEmpty()) {
            throw new BadRequestException("Skill and Job Requirements must not be empty");
        }

        // Map simple fields using MapStruct
        Job job = jobMapper.mapToJob(jobRequest);

        // Set Job Category (manual DB fetch)
        UUID categoryUuid = UUID.fromString(jobRequest.getJobCategory());
        JobCatagory jobCatagory = jobCataggoryRepository.findByUuid(categoryUuid)
                .orElseThrow(() -> new ResourNotFound("Invalid job category UUID"));
        job.setJobCatagory(jobCatagory);

        // Fetch and set Skill entities
        Set<Skill> skills = jobRequest.getSkills().stream()
                .map(skillDto -> skillRepository.findByNameIgnoreCase(skillDto.getName())
                        .orElseThrow(() -> new RuntimeException("Skill not found: " + skillDto.getName())))
                .collect(Collectors.toSet());
        job.setSkills(skills);

        // Map and set Job Requirements
        List<JobRequirement> requirements = jobRequest.getJobRequirements().stream()
                .map(reqDto -> {
                    JobRequirement req = new JobRequirement();
                    req.setRequirement(reqDto.getRequirement());
                    req.setJob(job);
                    return req;
                })
                .collect(Collectors.toList());
        job.setJobRequirements(requirements);

        // Save the Job entity
        Job savedJob = jobRepository.save(job);

        // Return full response
        return jobMapper.mapToJobRespone(savedJob);
    }

    @Override
    public JobRespone update(Long id, JobRequest jobRequest) {
        return null;
    }

    @Override
    public JobRespone getById(Long id) {
        return null;
    }

    @Override
    public List<JobRespone> getAll() {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }
}
