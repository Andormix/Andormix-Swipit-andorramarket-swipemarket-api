package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductRepository;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InteractionService
{

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductSwipeRepository swipeRepository;
    private final ProductFavoriteRepository favoriteRepository;

    public InteractionService(
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductSwipeRepository swipeRepository,
            ProductFavoriteRepository favoriteRepository
    ) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.swipeRepository = swipeRepository;
        this.favoriteRepository = favoriteRepository;
    }

    public SwipeResponse swipe(Long productId, SwipeRequest request, AppUserPrincipal principal)
    {
        Product product = getProduct(productId);
        User user = getUser(principal);

        validateNotOwnProduct(user, product);

        ProductSwipe swipe = new ProductSwipe(user, product, action);
        ProductSwipe savedSwipe = swipeRepository.save(swipe);

        SwipeResponse swipeResponse = new SwipeResponse(
                savedSwipe.getProductId(), savedSwipe.getAction(), swipe.getUpdatedAt()
        );

        return swipeResponse;
    }

    // Helpers

    private User getUser(AppUserPrincipal principal) {
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }

    private Product getUserProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }

    private Product getProduct(Long productId) {
        return getUserProduct(productId);
    }

    private void validateNotOwnProduct(User user, Product product) {
        if (product.getSeller().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot swipe your own product"
            );
        }
    }}


    /*
    * public record SwipeResponse(
        Long productId,
        SwipeAction action,
        Instant updatedAt
) {
}
    *
    * */

    // TO IMPLEMENT (FIRST THOUGHTS)
    // Set Swipe Product by User and Product
    // Mod Swipe Product by User and Product
    // Get all Swipes by User
    // Get specific Swipe by User and Product

    // Add favorite by User and Product
    // Remove favorite by User and Product
    // Get all favorites by User
    // Get specific Favorite by User and Product


}
