package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_userProfile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String gender;
    private String phoneNumber;
    private String address;
    private String dateOfBirth;
    private String bio;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String instagram;

}
