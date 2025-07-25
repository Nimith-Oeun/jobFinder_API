package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.mapper.JobMapper;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobCategory;
import persional.jobfinder_api.model.JobRequirement;
import persional.jobfinder_api.model.Skill;
import persional.jobfinder_api.repository.JobCataggoryRepository;
import persional.jobfinder_api.repository.JobRepository;
import persional.jobfinder_api.repository.SkillRepository;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.spec.FilterSpec;
import persional.jobfinder_api.spec.SearchFilterDTO;
import persional.jobfinder_api.spec.GlobleSearchSpec;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final JobRepository jobRepository;
    private final JobCataggoryRepository jobCataggoryRepository;
    private final SkillRepository skillRepository;

    @Override
    public JobResponse create(JobRequestDTO jobRequestDTO) {

        // Validate job category UUID
        if (!StringUtils.hasText(String.valueOf(jobRequestDTO.getJobCategoryUuid()))) {
            throw new BadRequestException("Job Category UUID must not be empty");
        }

        if (jobRequestDTO.getSkills().isEmpty() || jobRequestDTO.getJobRequirements().isEmpty()) {
            throw new BadRequestException("Skill and Job Requirements must not be empty");
        }

        // Check if all requested skills exist in the database
        List<String> dbSkillNames = skillRepository.findAll()
                .stream()
                .map(Skill::getName)
                .map(String::toLowerCase)
                .toList();

        List<String> requestedSkillNames = jobRequestDTO.getSkills()
                .stream()
                .map(skillDto -> skillDto.getName().toLowerCase())
                .toList();

        for (String skillName : requestedSkillNames) {
            if (!dbSkillNames.contains(skillName)) {
                throw new BadRequestException("Skill not found: " + skillName);
            }
        }

        // Map simple fields using MapStruct
        Job job = jobMapper.mapToJob(jobRequestDTO);

        // Set Job Category (manual DB fetch)
        JobCategory jobCategory = jobCataggoryRepository.findByUuid(jobRequestDTO.getJobCategoryUuid())
                .orElseThrow(() -> new ResourNotFound("Invalid job category UUID"));
        job.setJobCategory(jobCategory);

        // Fetch and set Skill entities
        Set<Skill> skills = jobRequestDTO.getSkills().stream()
                .map(skillDto -> skillRepository.findByNameIgnoreCase(skillDto.getName())
                        .orElseThrow(() -> new RuntimeException("Skill not found: " + skillDto.getName())))
                .collect(Collectors.toSet());
        job.setSkills(skills);

        // Map and set Job Requirements
        List<JobRequirement> requirements = jobRequestDTO.getJobRequirements().stream()
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
        return jobMapper.mapToJobResponse(savedJob);
    }


    @Override
    public Job getById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("Job not found with ID: " + id));
    }


    /*
     * Get all jobs with optional filtering by keyword or ID.
     */
    @Override
    public List<Job> searchjob(Map<String, String> param) {

        return getJobs(param);
    }

    @Override
    public List<Job> filter(Map<String, String> param) {
        return getJobs(param);
    }


    @Override
    public JobResponse update(Long id, JobRequestDTO jobRequestDTO) {
        return null;
    }


    @Override
    public void delete(Long id) {

    }

    private  List<Job> getJobs(Map<String, String> param) {

        SearchFilterDTO searchFilterDTO = new SearchFilterDTO();

        log.error("globle search param: {}", searchFilterDTO);

        if (param.containsKey("keyword")) {
            String keyword = param.get("keyword");
            searchFilterDTO.setKeyword(keyword);

            GlobleSearchSpec globleSearchSpec = new GlobleSearchSpec(searchFilterDTO);
            return jobRepository.findAll(globleSearchSpec);
        }

        if (param.containsKey("category")) {
            String category = param.get("category");
            searchFilterDTO.setCategory(category);

            FilterSpec filterSpec = new FilterSpec(searchFilterDTO);
            return jobRepository.findAll(filterSpec);
        }

        if (param.containsKey("location")) {
            String locations = param.get("location");
            searchFilterDTO.setLocation(locations);

            FilterSpec filterSpec = new FilterSpec(searchFilterDTO);
            return jobRepository.findAll(filterSpec);
        }

        if (param.containsKey("skill")) {
            String skill = param.get("skill");
            searchFilterDTO.setSkill(skill);

            FilterSpec filterSpec = new FilterSpec(searchFilterDTO);
            return jobRepository.findAll(filterSpec);
        }

        if (param.containsKey("id")) {
            Long id = Long.valueOf(param.get("id"));
            return Collections.singletonList(getById(id));
        }

        return Collections.emptyList();
    }
}
