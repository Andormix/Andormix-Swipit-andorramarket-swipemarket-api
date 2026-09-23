package com.andormix.swipemarketapi.interaction;

import jakarta.validation.constraints.NotNull;

public record SwipeRequest(
        @NotNull
        SwipeAction action
) {
}