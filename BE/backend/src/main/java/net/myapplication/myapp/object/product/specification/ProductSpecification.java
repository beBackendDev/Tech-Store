package net.myapplication.myapp.object.product.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

import net.myapplication.myapp.object.product.entity.LaptopSpecification;
import net.myapplication.myapp.object.product.entity.Product;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<Product> keyword(String keyword) {

        return (root, query, cb) -> {

            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String value = "%" + keyword.trim().toLowerCase() + "%";

            return cb.or(
                    cb.like(
                            cb.lower(root.get("name")),
                            value
                    ),
                    cb.like(
                            cb.lower(root.get("description")),
                            value
                    ),
                    cb.like(
                            cb.lower(root.get("externalId")),
                            value
                    )
            );
        };
    }

    public static Specification<Product> category(String category) {

        return (root, query, cb) -> {

            if (category == null || category.isBlank()) {
                return null;
            }

            return cb.equal(
                    cb.lower(root.get("category")),
                    category.trim().toLowerCase()
            );
        };
    }

    public static Specification<Product> brand(String brand) {

        return (root, query, cb) -> {

            if (brand == null || brand.isBlank()) {
                return null;
            }

            Join<Product, LaptopSpecification> laptop =
                    root.join("laptopSpecification", JoinType.INNER);

            return cb.equal(
                    cb.lower(laptop.get("brand")),
                    brand.trim().toLowerCase()
            );
        };
    }

    public static Specification<Product> minPrice(BigDecimal minPrice) {

        return (root, query, cb) -> {

            if (minPrice == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("price"),
                    minPrice
            );
        };
    }

    public static Specification<Product> maxPrice(BigDecimal maxPrice) {

        return (root, query, cb) -> {

            if (maxPrice == null) {
                return null;
            }

            return cb.lessThanOrEqualTo(
                    root.get("price"),
                    maxPrice
            );
        };
    }

    public static Specification<Product> minRating(BigDecimal minRating) {

        return (root, query, cb) -> {

            if (minRating == null) {
                return null;
            }

            return cb.greaterThanOrEqualTo(
                    root.get("rating"),
                    minRating
            );
        };
    }

    public static Specification<Product> active(Boolean active) {

        return (root, query, cb) -> {

            if (active == null) {
                return null;
            }

            return cb.equal(
                    root.get("active"),
                    active
            );
        };
    }
}