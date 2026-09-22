package com.andormix.swipemarketapi.product;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecifications implements Specification<Product> {

    private final ProductSearchCriteria criteria;

    public ProductSpecifications(ProductSearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        List<Predicate> predicates = new ArrayList<>();

        if (criteria.query() != null && !criteria.query().isBlank()) {
            String searchPattern = "%" + criteria.query().trim().toLowerCase() + "%";

            Predicate titleMatches = builder.like(
                    builder.lower(root.get("title")),
                    searchPattern
            );

            Predicate descriptionMatches = builder.like(
                    builder.lower(root.get("description")),
                    searchPattern
            );

            predicates.add(builder.or(titleMatches, descriptionMatches));
        }

        if (criteria.category() != null) {
            predicates.add(builder.equal(root.get("category"), criteria.category()));
        }

        if (criteria.condition() != null) {
            predicates.add(builder.equal(root.get("condition"), criteria.condition()));
        }

        if (criteria.parish() != null && !criteria.parish().isBlank()) {
            predicates.add(
                    builder.equal(
                            builder.lower(root.get("parish")),
                            criteria.parish().trim().toLowerCase()
                    )
            );
        }

        if (criteria.status() != null) {
            predicates.add(builder.equal(root.get("status"), criteria.status()));
        }

        if (criteria.minPrice() != null) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("price"), criteria.minPrice()));
        }

        if (criteria.maxPrice() != null) {
            predicates.add(builder.lessThanOrEqualTo(root.get("price"), criteria.maxPrice()));
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}