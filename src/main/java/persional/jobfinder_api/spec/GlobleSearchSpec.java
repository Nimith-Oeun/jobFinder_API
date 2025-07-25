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

    private final SearchFilterDTO searchFilterDTO;
    List<Predicate> predicates = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<Job> job, CriteriaQuery<?> query, CriteriaBuilder cb) {

        if (searchFilterDTO.getKeyword() != null && !searchFilterDTO.getKeyword().isEmpty()) {
                predicates.add(
                        cb.like(cb.upper(job.get("title")), "%" + searchFilterDTO.getKeyword().toUpperCase() + "%")
                );
        }

        if (searchFilterDTO.getId() != null) {
                predicates.add(cb.equal(job.get("id"), searchFilterDTO.getId()));
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }
}
