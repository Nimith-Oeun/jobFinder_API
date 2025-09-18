package persional.jobfinder_api.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobApply;

import java.util.List;
import java.util.Optional;

public interface JobApplyRepository extends JpaRepository<JobApply, Long> {

    // OR if you only want job info directly:
    @Query("SELECT ja.job FROM JobApply ja WHERE ja.profile.id = :profileId")
    List<Job> findJobsAppliedByProfileId(@Param("profileId") Long profileId);
}
