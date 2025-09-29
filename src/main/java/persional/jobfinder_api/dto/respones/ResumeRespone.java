package persional.jobfinder_api.dto.respones;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeRespone {

    private Long id;

    private String fileType;

    private String fileFomate;

    private double fileSize;

    private String fileName;

}
