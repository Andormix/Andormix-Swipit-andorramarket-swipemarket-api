package com.andormix.swipemarketapi.reviews;

import java.time.Instant;

public record ReviewResponse(

        Long userId,
        Integer rating,
        String comment,
        Instant createdAt

) {
}
