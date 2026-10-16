package com.secureflow.secureflow_backend.audit.specification;

import com.secureflow.secureflow_backend.audit.entity.AuditAction;
import com.secureflow.secureflow_backend.audit.entity.AuditLog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditSpecification {

    private AuditSpecification() {
    }

    public static Specification<AuditLog> filter(
            AuditAction action,
            String entityName,
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (action != null) {
                predicates.add(
                        cb.equal(root.get("action"), action)
                );
            }

            if (entityName != null && !entityName.isBlank()) {
                predicates.add(
                        cb.equal(
                                cb.lower(root.get("entityName")),
                                entityName.trim().toLowerCase()
                        )
                );
            }

            if (userId != null) {
                predicates.add(
                        cb.equal(root.get("user").get("id"), userId)
                );
            }

            if (startDate != null) {
                predicates.add(
                        cb.greaterThanOrEqualTo(
                                root.get("createdAt"),
                                startDate
                        )
                );
            }

            if (endDate != null) {
                predicates.add(
                        cb.lessThanOrEqualTo(
                                root.get("createdAt"),
                                endDate
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
