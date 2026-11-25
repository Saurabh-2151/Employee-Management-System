
package com.example.employee_management_system.repository;

import com.example.employee_management_system.entity.Employee;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> filterEmployees(
            String search,
            Long departmentId,
            Boolean active
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Search by employee name or email
            if (search != null && !search.isBlank()) {

                String keyword = "%" +
                        search.trim().toLowerCase() + "%";

                Predicate firstName = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("firstName")),
                        keyword
                );

                Predicate lastName = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("lastName")),
                        keyword
                );

                Predicate email = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("email")),
                        keyword
                );

                Predicate fullName = criteriaBuilder.like(
                        criteriaBuilder.lower(
                                criteriaBuilder.concat(
                                    criteriaBuilder.concat(
                                        root.get("firstName"), " "
                                    ),
                                    root.get("lastName")
                                )
                        ),
                        keyword
                );

                predicates.add(
                        criteriaBuilder.or(
                                firstName,
                                lastName,
                                email,
                                fullName
                        )
                );
            }

            // Filter by department
            if (departmentId != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("department").get("id"),
                                departmentId
                        )
                );
            }

            // Filter by active status
            if (active != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("active"),
                                active
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}
