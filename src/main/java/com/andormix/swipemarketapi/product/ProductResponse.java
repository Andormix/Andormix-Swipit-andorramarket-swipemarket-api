package com.andormix.swipemarketapi.product;

import java.math.BigDecimal;
import java.time.Instant;

public record ProductResponse(
        Long id,
        Long sellerId,
        String sellerDisplayName,
        String title,
        String description,
        BigDecimal price,
        ProductCategory category,
        ProductCondition condition,
        String parish,
        ProductStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}