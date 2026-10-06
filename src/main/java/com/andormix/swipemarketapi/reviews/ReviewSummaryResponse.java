package com.andormix.swipemarketapi.reviews;

import java.util.List;

public record ReviewSummaryResponse(

        Long productId,
        Double averageRating,
        Long totalReviews,
        List<ReviewResponse> recentReviews
) {
}
