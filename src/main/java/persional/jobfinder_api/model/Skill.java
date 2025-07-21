package persional.jobfinder_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import persional.jobfinder_api.common.BaseEntity;

import java.util.List;

@Data
@Entity
@Table(name = "tb_skill")
public class Skill extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "skills")
    @JsonIgnore
    private List<Job> jobs;
}