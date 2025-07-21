package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import persional.jobfinder_api.common.BaseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tb_job")
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String company;

    private String thumbnail;

    private String location;

    private double salary;

    @Column(name = "application_instr")
    private String appliationInstr;

    @Column(name = "job_type")
    private String jobType;

    @ManyToOne
    @JoinColumn(
            name = "job_category_id"
    )
    private JobCategory jobCategory;

    @ManyToMany
    @JoinTable(
            name = "job_skill",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private Set<Skill> skills = new HashSet<>();

    @OneToMany(mappedBy = "job" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JobRequirement> jobRequirements = new ArrayList<>();

}