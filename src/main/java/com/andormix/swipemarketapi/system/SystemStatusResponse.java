package com.andormix.swipemarketapi.system;

import java.time.Instant;

public record SystemStatusResponse(
        String status,
        String application,
        Instant timestamp
) {
}