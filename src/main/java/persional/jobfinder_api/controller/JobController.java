package persional.jobfinder_api.controller;

import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.mapper.JobMapper;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.SkillService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobfinder_api/v1/job")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;
    private final SkillService skillService;
    private final JobCategoryService jobCategoryService;
    private final JobMapper jobMapper;

    @RolesAllowed({"ADMIN"})
    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody JobRequestDTO jobRequestDTO){
        JobResponse jobRespone = jobService.create(jobRequestDTO);
        log.info("Job created successfully: {}", jobRespone);
        return ResponseEntity.ok(SuccessRespone.success(jobRespone));
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping("/create/skill")
    public ResponseEntity<?> createSkill(@RequestBody SkillRequest skillRequest){
        skillService.create(skillRequest);
        return ResponseEntity.ok(SuccessRespone.success("Skill created successfully"));
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping("/create/category")
    public ResponseEntity<?> createCategory(@RequestBody JobCategoryRequest jobCategoryRequest){
        jobCategoryService.create(jobCategoryRequest);
        return ResponseEntity.ok(SuccessRespone.success("Category created successfully"));
    }

    @GetMapping("/globle-search")
    public ResponseEntity<?> globleSearch(@RequestParam Map<String,String> search) {
        List<JobResponse> jobList = jobService.searchjob(search)
                .stream().map(jobMapper::mapToJobResponse)
                .toList();
        return ResponseEntity.ok(SuccessRespone.success(jobList));
    }
}
