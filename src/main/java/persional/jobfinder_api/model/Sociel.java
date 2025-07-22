package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_social")
public class Sociel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "facebook" )
    private String facebook;

    @Column(name = "twitter" )
    private String twitter;

    @Column(name = "instagram" )
    private String instagram;

    @Column(name = "lingIn" )
    private String linkedIn;

}
