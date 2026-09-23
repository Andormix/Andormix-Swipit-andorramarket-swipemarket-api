package com.andormix.swipemarketapi.interaction;

import java.time.Instant;

public record SwipeResponse(
        Long productId,
        SwipeAction action,
        Instant updatedAt
) {
}