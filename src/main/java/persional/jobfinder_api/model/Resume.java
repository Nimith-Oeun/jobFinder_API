package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_resume")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_Type")
    private String fileType;

    @Column(name = "file_Fomate")
    private String fileFomate;

    @Column(name = "file_Size")
    private double fileSize;

    @Column(name = "file_Name")
    private String fileName;

    @Column(name = "Part_Upload")
    private String partUpload;

}
