package persional.jobfinder_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.request.JobCategoryRequest;
import persional.jobfinder_api.dto.request.JobRequestDTO;
import persional.jobfinder_api.dto.request.SkillRequest;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.service.JobApplyService;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.SkillService;

@RestController
@RequestMapping("/jobfinder_api/v1/job-Apply")
@RequiredArgsConstructor
@Slf4j
public class JobApplyController {

    private final JobApplyService jobApplyService;

    @PostMapping("")
    public ResponseEntity<?> applyJob(@RequestBody JobApplyRequestDTO jobApplyRequestDTO){
        JobApplyRespone jobApply = jobApplyService.createJobApply(jobApplyRequestDTO);
        log.info("Job created successfully: {}", jobApplyRequestDTO);
        return ResponseEntity.ok(SuccessRespone.success(jobApply));
    }
}
