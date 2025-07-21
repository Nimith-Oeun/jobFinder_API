package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.mapper.JobApplyMapper;
import persional.jobfinder_api.model.JobApply;
import persional.jobfinder_api.repository.JobApplyRepository;
import persional.jobfinder_api.repository.JobRepository;
import persional.jobfinder_api.service.JobApplyService;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApplyRepository jobApplyRepository;
    private final JobApplyMapper jobApplyMapper;
    private final JobRepository jobRepository;

    @Override
    public JobApplyRespone createJobApply(JobApplyRequestDTO request) {

        if (request.getJobId() == null || request.getResume() == null){
            log.error("Job ID or Resume is null in request: {}", request);
            throw new InternalServerError("Job ID and Resume must not be null");
        }

        if (jobRepository.findById(Long.valueOf(request.getJobId())).isEmpty()) {
            log.error("Job not found with ID: {}", request.getJobId());
            throw new InternalServerError("Job not found with ID: " + request.getJobId());
        }

        JobApply jobApply = jobApplyMapper.mapToJobApply(request);
        jobApply = jobApplyRepository.save(jobApply);


        return jobApplyMapper.mapToJobApplyRespone(jobApply);
    }

    @Override
    public JobApply getJobApplyById(Long id) {
        return jobApplyRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("Job apply not found with id: " + id));
    }
}
