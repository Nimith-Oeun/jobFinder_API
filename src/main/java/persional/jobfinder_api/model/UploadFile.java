package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import persional.jobfinder_api.common.BaseEntity;

@Data
@Entity
@Table(name = "tbl_uploadFile")
public class UploadFile extends BaseEntity {

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

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile profile;

}