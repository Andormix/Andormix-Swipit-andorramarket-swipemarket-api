package com.andormix.swipemarketapi.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateProductRequest(

        @NotBlank
        @Size(max = 120)
        String title,

        @NotBlank
        @Size(max = 2000)
        String description,

        @NotNull
        @DecimalMin(value = "0.00")
        BigDecimal price,

        @NotNull
        ProductCategory category,

        @NotNull
        ProductCondition condition,

        @NotBlank
        @Size(max = 50)
        String parish
) {
}