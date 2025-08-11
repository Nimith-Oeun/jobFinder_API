package persional.jobfinder_api.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static persional.jobfinder_api.enums.Permission.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Role {

    ADMIN(Set.of(
            JOB_WRITE ,
            JOB_READ,
            APPLY_READ,
            SKILL_WRITE,
            SKILL_READ,
            SOCIAL_WRITE,
            SOCIAL_READ,
            CATEGORY_WRITE,
            CATEGORY_READ,
            UPLOAD_READ
             )
    ),
    USER(Set.of(
            JOB_READ,
            APPLY_WRITE,
            APPLY_READ,
            SKILL_READ,
            SOCIAL_READ,
            CATEGORY_READ,
            UPLOAD_WRITE
    ));

    private Set<Permission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        Set<SimpleGrantedAuthority> grantedAuthorities = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getDescription()))
                .collect(Collectors.toSet());

        // Add the role to the authorities
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return grantedAuthorities;
    }

}
