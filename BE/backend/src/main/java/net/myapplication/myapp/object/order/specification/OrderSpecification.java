package net.myapplication.myapp.object.order.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

import net.myapplication.myapp.object.order.entity.Order;
import net.myapplication.myapp.user.dto.request.AdminOrderFilterRequest;

public class OrderSpecification {

    public static Specification<Order> filter(
            AdminOrderFilterRequest filter
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates =
                    new ArrayList<>();

            // =====================================
            // STATUS
            // =====================================

            if (filter.getStatus() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                filter.getStatus()
                        )
                );
            }

            // =====================================
            // PAYMENT STATUS
            // =====================================

            if (filter.getPaymentStatus() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("paymentStatus"),
                                filter.getPaymentStatus()
                        )
                );
            }

            // =====================================
            // KEYWORD
            // =====================================

            if (filter.getKeyword() != null
                    && !filter.getKeyword().isBlank()) {

                String keyword =
                        "%" + filter.getKeyword()
                                .toLowerCase()
                                .trim()
                                + "%";

                predicates.add(

                        criteriaBuilder.or(

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("customerName")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("email")
                                        ),
                                        keyword
                                ),

                                criteriaBuilder.like(
                                        criteriaBuilder.lower(
                                                root.get("phone")
                                        ),
                                        keyword
                                )
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}
