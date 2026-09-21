package com.andormix.swipemarketapi.product;

import jakarta.validation.constraints.NotNull;

public record ChangeProductStatusRequest(
        @NotNull
        ProductStatus status
) {
}