package persional.jobfinder_api.dto.respones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRespone {
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
