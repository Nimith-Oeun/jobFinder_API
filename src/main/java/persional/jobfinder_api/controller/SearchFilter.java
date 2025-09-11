package persional.jobfinder_api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import persional.jobfinder_api.dto.respones.JobResponse;
import persional.jobfinder_api.exception.SuccessRespone;
import persional.jobfinder_api.mapper.JobMapper;
import persional.jobfinder_api.service.JobCategoryService;
import persional.jobfinder_api.service.JobService;
import persional.jobfinder_api.service.SkillService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobfinder_api/v1")
@RequiredArgsConstructor
@Slf4j
public class SearchFilter {

    private final JobService jobService;
    private final JobMapper jobMapper;

    @GetMapping("/globle-search")
    public ResponseEntity<?> globleSearch(@RequestParam Map<String,String> search) {
        List<JobResponse> jobList = jobService.searchjob(search);
        return ResponseEntity.ok(SuccessRespone.success(jobList));
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestParam Map<String,String> search) {
        List<JobResponse> jobList = jobService.filter(search);
        return ResponseEntity.ok(SuccessRespone.success(jobList));
    }
}
