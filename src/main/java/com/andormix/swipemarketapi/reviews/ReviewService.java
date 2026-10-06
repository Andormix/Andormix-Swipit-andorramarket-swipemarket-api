package com.andormix.swipemarketapi.reviews;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductRepository;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public ReviewResponse create(AppUserPrincipal principal, ReviewRequest reviewRequest, Long productId)
    {
        Product product = getProduct(productId);
        User user = getUser(principal.getUserId());

        // Producto del mismo usuario
        if(product.getSeller().getId().equals(principal.getUserId()))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You can not review your own product"
            );
        }

        // Producto ya revieweado por el usuaro
        if(reviewRepository.existsByProductIdAndUserId(product.getId(), user.getId()))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You can not review multiple times the same product"
            );
        }

        Review review = new Review(
        product,
        user,
        reviewRequest.rating(),
        reviewRequest.comment()
        );

        return toReviewResponse(reviewRepository.save(review));
    }

    public ReviewSummaryResponse reviewSummaryResponseList(Long productId)
    {
        //Validación
        getProduct(productId);

        Double avg = reviewRepository.findAverageRatingByProductId(productId).orElse(0.0);
        Long count = reviewRepository.countByProductId(productId);
        Pageable pageable = PageRequest.of(0, 5);
        List<Review> reviews = reviewRepository.findRecentByProductId(productId, pageable);

        List<ReviewResponse> recentReviews = toReviewResponseList(reviews);

        return new ReviewSummaryResponse(
                productId,
                avg,
                count,
                recentReviews

        );
    }

    // ------------------------ HELPERS --------------------------

    private User getUser(Long userId)
    {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User Not Found"
                        )
                );
    }

    private Product getProduct(Long productId)
    {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product Not Found"
                        )

                );
    }

    private ReviewResponse toReviewResponse(Review r)
    {
        return new ReviewResponse(
            r.getUser().getId(),
            r.getRating(),
            r.getComment(),
            r.getCreatedAt()
        );
    }

    private List<ReviewResponse> toReviewResponseList(List<Review> reviews)
    {
        return reviews.stream().map(this::toReviewResponse).toList();
    }

}
