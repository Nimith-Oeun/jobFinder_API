package persional.jobfinder_api.spec;

import jakarta.persistence.criteria.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import persional.jobfinder_api.model.Job;
import persional.jobfinder_api.model.JobCategory;
import persional.jobfinder_api.model.Skill;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class FilterSpec implements Specification<Job> {

    private final SearchFilterDTO searchFilterDTO;


    @Override
    public Predicate toPredicate(Root<Job> job, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();
        Join<Job , JobCategory> categoryJoins = job.join("jobCategory");
        Join<Job , Skill> skillJoins = job.join("skills", JoinType.LEFT);

        if (searchFilterDTO.getCategory() != null && !searchFilterDTO.getCategory().isEmpty()) {
            predicates.add(cb.equal(cb.upper(categoryJoins.get("name")), searchFilterDTO.getCategory().toUpperCase()));
        }

        if (searchFilterDTO.getLocation() != null && !searchFilterDTO.getLocation().isEmpty()) {
            predicates.add(cb.equal(cb.upper(job.get("location")), searchFilterDTO.getLocation().toUpperCase()));
        }

        if (searchFilterDTO.getSkill() != null && !searchFilterDTO.getSkill().isEmpty()) {
            predicates.add(cb.equal(cb.upper(skillJoins.get("name")), searchFilterDTO.getSkill().toUpperCase()));
        }


        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
