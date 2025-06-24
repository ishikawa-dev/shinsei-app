package com.example.demo.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.model.T_HEADER;

public class HeaderSpecification {

    public static Specification<T_HEADER> searchByConditions(String userId, String submissionDateFrom, String submissionDateTo, String approvalStatus) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (userId != null && !userId.isEmpty()) {
                predicates.add(cb.equal(root.get("userId"), userId));
            }

            if ((submissionDateFrom != null && !submissionDateFrom.isEmpty()) ||
                (submissionDateTo != null && !submissionDateTo.isEmpty())) {

                if (submissionDateFrom != null && !submissionDateFrom.isEmpty()) {
                    LocalDate fromDate = LocalDate.parse(submissionDateFrom);
                    predicates.add(cb.greaterThanOrEqualTo(root.get("submissionDate"), fromDate));
                }

                if (submissionDateTo != null && !submissionDateTo.isEmpty()) {
                    LocalDate toDate = LocalDate.parse(submissionDateTo);
                    predicates.add(cb.lessThanOrEqualTo(root.get("submissionDate"), toDate));
                }
            }

            if (approvalStatus != null && !approvalStatus.isEmpty()) {
                Integer status = Integer.valueOf(approvalStatus);
                predicates.add(cb.equal(root.get("approvalStatus"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}