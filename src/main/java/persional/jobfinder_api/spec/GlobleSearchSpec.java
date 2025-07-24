package persional.jobfinder_api.spec;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import persional.jobfinder_api.model.Job;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class GlobleSearchSpec implements Specification<Job> {

    private final GlobleSearch globleSearch;
    List<Predicate> predicates = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<Job> job, CriteriaQuery<?> query, CriteriaBuilder cb) {

        if (globleSearch.getKeyword() != null && !globleSearch.getKeyword().isEmpty()) {
                predicates.add(
                        cb.like(cb.upper(job.get("title")), "%" + globleSearch.getKeyword().toUpperCase() + "%")
                );
        }

        if (globleSearch.getId() != null) {
                predicates.add(cb.equal(job.get("id"), globleSearch.getId()));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
