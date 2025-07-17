package persional.jobfinder_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "tb_jobCatagory")
public class JobCatagory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    private String name;

    @OneToMany(mappedBy = "jobCatagory")
    @JsonIgnore
    private List<Job> jobs;

    @PrePersist
    public void generateUUID() {
        this.uuid = UUID.randomUUID();
    }
}