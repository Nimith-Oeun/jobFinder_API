package persional.jobfinder_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.dto.respones.JobApplyResponeForClient;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.service.JobApplyService;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.SkillService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/jobfinder_api/v1/job-Apply")
@RequiredArgsConstructor
@Slf4j
public class JobApplyController {

    private final JobApplyService jobApplyService;

    @PreAuthorize("hasAuthority('apply:write')")
    @PostMapping("/apply")
    public ResponseEntity<?> applyJob(@RequestBody JobApplyRequestDTO jobApplyRequestDTO){
        JobApplyRespone jobApply = jobApplyService.createJobApply(jobApplyRequestDTO);
        log.info("Job created successfully: {}", jobApplyRequestDTO);
        return ResponseEntity.ok(SuccessRespone.success(jobApply));
    }

    @PreAuthorize("hasAuthority('apply:read')")
    @GetMapping("")
    public ResponseEntity<?>getJobByCurrentUser(Principal principal){
        log.info("Get name current user: {}", principal.getName());
        List<JobApplyResponeForClient> jobByCurrentUser = jobApplyService.getJobByCurrentUser(principal);
        return ResponseEntity.ok(SuccessRespone.success(jobByCurrentUser));
    }
}
