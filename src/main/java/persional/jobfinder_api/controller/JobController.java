package persional.jobfinder_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.dto.request.JobRequest;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.dto.respones.JobRespone;
import persional.jobfinder_api.model.Skill;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.SkillService;

@RestController
@RequestMapping("/jobfinder_api/v1/job")
@RequiredArgsConstructor
@Slf4j
public class JobController {

    private final JobService jobService;
    private final SkillService skillService;
    private final JobCategoryService jobCategoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createJob(@RequestBody JobRequest jobRequest){
        JobRespone jobRespone = jobService.create(jobRequest);
        log.info("Job created successfully: {}", jobRespone);
        return ResponseEntity.ok(jobRespone);
    }
    @PostMapping("/create/skill")
    public ResponseEntity<?> createSkill(@RequestBody SkillRequest skillRequest){
        skillService.create(skillRequest);
        return ResponseEntity.ok("Skill created successfully: ");
    }

    @PostMapping("/create/category")
    public ResponseEntity<?> createCategory(@RequestBody JobCategoryRequest jobCategoryRequest){
        jobCategoryService.create(jobCategoryRequest);
        return ResponseEntity.ok("Category created successfully: ");
    }
}
