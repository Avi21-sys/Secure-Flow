package com.secureflow.secureflow_backend.incident.specification;

import com.secureflow.secureflow_backend.incident.entity.Incident;
import com.secureflow.secureflow_backend.incident.entity.IncidentSeverity;
import com.secureflow.secureflow_backend.incident.entity.IncidentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class IncidentSpecification {

    private IncidentSpecification() {
    }

    public static Specification<Incident> filter(
            IncidentStatus status,
            IncidentSeverity severity,
            Long vulnerabilityId,
            Long assignedToId
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(
                        cb.equal(root.get("status"), status)
                );
            }

            if (severity != null) {
                predicates.add(
                        cb.equal(root.get("severity"), severity)
                );
            }

            if (vulnerabilityId != null) {
                predicates.add(
                        cb.equal(
                                root.get("vulnerability").get("id"),
                                vulnerabilityId
                        )
                );
            }

            if (assignedToId != null) {
                predicates.add(
                        cb.equal(
                                root.get("assignedTo").get("id"),
                                assignedToId
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
