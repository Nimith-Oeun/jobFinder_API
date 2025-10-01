package persional.jobfinder_api.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
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


    @CacheEvict(value = "jobApplies" , allEntries = true)
    @Override
    public JobApplyRespone createJobApply(JobApplyRequestDTO request) {

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        if (request.getJobId() == null || request.getResumeId() == null){
            log.error("Job ID or Resume is null in request: {}", request);
            throw new InternalServerError("Job ID and Resume must not be null");
        }

        Job job = jobRepository.findById(Long.valueOf(request.getJobId()))
                .orElseThrow(() -> new ResourNotFound("Job not found with id: " + request.getJobId()));

        Resume resume = resumeRopository.findById(Long.valueOf(request.getResumeId()))
                .orElseThrow(() -> new ResourNotFound("Resume not found with id: " + request.getResumeId()));

        UserProfile userProfile = userProfileRepository.findByEmail(name)
                .orElseThrow(() -> new ResourNotFound("User not found with email: " + name));

        // Check if the user has already applied for the job
        jobApplyRepository.findByJobIdAndProfileId(Long.valueOf(request.getJobId()), userProfile.getId())
                .ifPresent(existingApply -> {
                    throw new BadRequestException("You have already applied for this positions at Company: " + job.getCompany());
                });


        JobApply jobApply = new JobApply();

        jobApply.setJob(job);
        jobApply.setResume(resume);
        jobApply.setProfile(userProfile);

        jobApply = jobApplyRepository.save(jobApply);


        return jobApplyMapper.mapToJobApplyRespone(jobApply);
    }

    @Override
    public JobApply getJobApplyById(Long id) {
        return jobApplyRepository.findById(id)
                .orElseThrow(() -> new ResourNotFound("Job apply not found with id: " + id));
    }

    @Cacheable(value = "jobApplies" , key = "#principal.name")
    @Override
    public List<JobApplyResponeForClient> getJobByCurrentUser(Principal principal) {

        UserProfile userProfile = userProfileRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourNotFound("User not found with email: " + principal.getName()));

        List<Job> jobList = userProfile.getApplyJobs().stream()
                .map(JobApply::getJob)
                .toList();

        List<JobApplyResponeForClient> responeForClients = jobList.stream()
                .map(job -> JobApplyResponeForClient.builder()
                        .jobId(job.getId().intValue())
                        .title(job.getTitle())
                        .company(job.getCompany())
                        .location(job.getLocation())
                        .build())
                .toList();

        /**
         * this code is used with custom query in JobApplyRepository
         * but it is less efficient than the above code because it makes two queries to the database
         * one to get the user profile and another to get the jobs applied by the user profile
         * while the above code makes only one query to get the user profile and then uses the
         * relationship mapping to get the jobs applied by the user profile
         * so Performance winner: findJobsAppliedByProfileId (custom query).
         * Flexibility: userProfile.getApplyJobs().


        List<Job> appliedByProfileId = jobApplyRepository.findJobsAppliedByProfileId(userProfile.getId());

        List<JobApplyResponeForClient> responeForClients = appliedByProfileId.stream()
                .map(job -> JobApplyResponeForClient.builder()
                        .title(job.getTitle())
                        .company(job.getCompany())
                        .location(job.getLocation())
                        .build())
                .toList();

            **/

        return responeForClients;
    }

    @CacheEvict(value = "jobApplies" , allEntries = true)
    @Override
    public void deleteJobApply(Long id) {

        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile currentUserProfile = userProfileRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourNotFound("User not found with email: " + currentUserEmail));

        JobApply apply = jobApplyRepository.findByJobIdAndProfileId(id , currentUserProfile.getId())
                .orElseThrow(() -> new BadRequestException("You have not applied for this job or job apply not found with id: " + id));

        jobApplyRepository.delete(apply);

    }
}
