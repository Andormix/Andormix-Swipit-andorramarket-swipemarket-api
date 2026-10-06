package com.andormix.swipemarketapi.reviews;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products/{productId}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping()
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal  AppUserPrincipal principal,
            @Valid @RequestBody ReviewRequest reviewRequest,
            @PathVariable Long productId)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.create(principal, reviewRequest, productId));
    }

    @GetMapping("/summary")
    public ResponseEntity<ReviewSummaryResponse> returnTop5Reviews(@PathVariable Long productId)
    {
        return ResponseEntity.status(HttpStatus.OK).body(reviewService.reviewSummaryResponseList(productId));
    }


}
