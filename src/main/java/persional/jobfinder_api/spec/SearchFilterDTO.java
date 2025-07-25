package persional.jobfinder_api.spec;

import lombok.Data;

@Data
public class SearchFilterDTO {

    private String keyword;

    private Integer id;

    private String category;

    private String location;

    private String skill;
}
