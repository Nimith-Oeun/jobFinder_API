package persional.jobfinder_api.model;

import jakarta.persistence.*;
import lombok.Data;
import persional.jobfinder_api.common.BaseEntity;
import persional.jobfinder_api.enums.Role;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tbl_userProfile")
public class UserProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

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

    @Enumerated(EnumType.STRING)
    private Role role;

    private String otp;

    private LocalDateTime otpExpiry;

    private LocalDateTime lastSendOtp;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled = false;

}
