package persional.jobfinder_api.dto.respones;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class JobApplyResponeForClient {

    private String title;
    private String company;
    private String location;

}
