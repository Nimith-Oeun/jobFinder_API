package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "tb_jobRequirement")
public class JobRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String Requirement;

    @ManyToOne
    @JoinColumn(
        name = "job_id"
    )
    private Job job;
}