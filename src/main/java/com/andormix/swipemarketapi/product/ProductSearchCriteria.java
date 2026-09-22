package com.andormix.swipemarketapi.product;


import java.math.BigDecimal;

public record ProductSearchCriteria(
        String query,
        ProductCategory category,
        ProductCondition condition,
        String parish,
        ProductStatus status,
        BigDecimal minPrice,
        BigDecimal maxPrice
) {
}