package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import persional.jobfinder_api.dto.request.JobApplyRequestDTO;
import persional.jobfinder_api.dto.respones.JobApplyRespone;
import persional.jobfinder_api.dto.respones.JobApplyResponeForClient;
import persional.jobfinder_api.exception.BadRequestException;
import persional.jobfinder_api.exception.InternalServerError;
import persional.jobfinder_api.exception.ResourNotFound;
import persional.jobfinder_api.mapper.JobApplyMapper;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobApply;
import persional.jobfinder_api.model.Resume;
import persional.jobfinder_api.model.UserProfile;
import persional.jobfinder_api.repository.JobApplyRepository;
import persional.jobfinder_api.repository.JobRepository;
import persional.jobfinder_api.repository.ResumeRopository;
import persional.jobfinder_api.repository.UserProfileRepository;
import persional.jobfinder_api.service.JobApplyService;
import persional.jobfinder_api.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplyServiceImpl implements JobApplyService {

    private final JobApplyRepository jobApplyRepository;
    private final JobApplyMapper jobApplyMapper;
    private final JobRepository jobRepository;
    private final ResumeRopository resumeRopository;
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    @Override
    public JobApplyRespone createJobApply(JobApplyRequestDTO request) {

        String name = userService.getCurrentUserProfile().getEmail();

        Optional<Job> job = jobRepository.findById(Long.valueOf(request.getJobId()));
        Optional<Resume> resume = resumeRopository.findById(Long.valueOf(request.getResumeId()));
        Optional<UserProfile> userProfile = userProfileRepository.findByEmail(name);


        if (request.getJobId() == null || request.getResumeId() == null){
            log.error("Job ID or Resume is null in request: {}", request);
            throw new InternalServerError("Job ID and Resume must not be null");
        }

        if (job.isEmpty()) {
            log.error("Job not found with ID: {}", request.getJobId());
            throw new ResourNotFound("Job not found with ID: " + request.getJobId());
        }
        if (resume.isEmpty()) {
            log.error("Resume not found with ID: {}", request.getResumeId());
            throw new BadRequestException("Resume not found with ID: " + request.getResumeId());
        }

        if (userProfile.isEmpty()) {
            log.error("UserProfile not found with email: {}", name);
            throw new BadRequestException("UserProfile not found with email: " + name);
        }


        JobApply jobApply = new JobApply();

        jobApply.setJob(job.get());
        jobApply.setResume(resume.get());
        jobApply.setProfile(userProfile.get());

        jobApply = jobApplyRepository.save(jobApply);


        return jobApplyMapper.mapToJobApplyRespone(jobApply);
    }

    @Override
    public JobApply getJobApplyById(Long id) {
        return jobApplyRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("Job apply not found with id: " + id));
    }

    @Override
    public List<JobApplyResponeForClient> getJobByCurrentUser(Principal principal) {

        UserProfile userProfile = userProfileRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourNotFound("User not found with email: " + principal.getName()));

        List<Job> appliedByProfileId = jobApplyRepository.findJobsAppliedByProfileId(userProfile.getId());

        List<JobApplyResponeForClient> responeForClients = appliedByProfileId.stream()
                .map(job -> JobApplyResponeForClient.builder()
                        .title(job.getTitle())
                        .company(job.getCompany())
                        .location(job.getLocation())
                        .build())
                .toList();


        return responeForClients;
    }
}
