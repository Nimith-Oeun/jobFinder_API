package persional.jobfinder_api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Permission {

    JOB_READ("job:read"),
    JOB_WRITE("job:write"),

    APPLY_READ("apply:read"),
    APPLY_WRITE("apply:write"),

    SKILL_READ("skill:read"),
    SKILL_WRITE("skill:write"),

    SOCIAL_READ("social:read"),
    SOCIAL_WRITE("social:write"),

    UPLOAD_READ("upload:read"),
    UPLOAD_WRITE("upload:write"),

    CATEGORY_READ("category:read"),
    CATEGORY_WRITE("category:write");

    private String description;


}
