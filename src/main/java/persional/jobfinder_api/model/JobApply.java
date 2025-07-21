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

    private String resume;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;
}
