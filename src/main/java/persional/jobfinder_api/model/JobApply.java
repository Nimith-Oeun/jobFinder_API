package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;
import persional.jobfinder_api.common.BaseEntity;


@Data
@Entity
@Table(name = "tb_jobApply")
public class JobApply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_file_id")
    private UploadFile resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private UserProfile profile;
}
