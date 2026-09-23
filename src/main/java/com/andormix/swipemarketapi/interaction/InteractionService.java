package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductRepository;
import com.andormix.swipemarketapi.product.ProductResponse;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class InteractionService {

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

    public SwipeResponse swipe(
            Long productId,
            SwipeRequest request,
            AppUserPrincipal principal
    ) {
        User user = getUser(principal);
        Product product = getProduct(productId);

        validateNotOwnProduct(user, product);

        ProductSwipe swipe = swipeRepository
                .findByUserAndProduct(user, product)
                .orElseGet(() -> new ProductSwipe(
                        user,
                        product,
                        request.action()
                ));

        swipe.changeAction(request.action());

        ProductSwipe savedSwipe = swipeRepository.save(swipe);

        return new SwipeResponse(
                savedSwipe.getProductId(),
                savedSwipe.getAction(),
                savedSwipe.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findInterestedProducts(
            AppUserPrincipal principal
    ) {
        User user = getUser(principal);

        return swipeRepository
                .findByUserAndAction(user, SwipeAction.LIKE)
                .stream()
                .map(swipe -> toProductResponse(
                        getProduct(swipe.getProductId())
                ))
                .toList();
    }

    public void addFavorite(
            Long productId,
            AppUserPrincipal principal
    ) {
        User user = getUser(principal);
        Product product = getProduct(productId);

        if (product.getSeller().getId().equals(user.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot favorite your own product"
            );
        }

        if (!favoriteRepository.existsByUserAndProduct(user, product)) {
            favoriteRepository.save(
                    new ProductFavorite(user, product)
            );
        }
    }

    public void removeFavorite(
            Long productId,
            AppUserPrincipal principal
    ) {
        User user = getUser(principal);
        Product product = getProduct(productId);

        favoriteRepository.deleteByUserAndProduct(user, product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findFavorites(AppUserPrincipal principal) {

        User user = getUser(principal);

        return favoriteRepository.findByUser(user)
                .stream()// el for
                // Por cada 'favorite' de la lista, extrae su producto y conviértelo a DTO
                .map(favorite -> toProductResponse(favorite.getProduct()))
                .toList();
    }

   /* @Transactional(readOnly = true)
    public List<ProductResponse> findFavorites(AppUserPrincipal principal) {
        User user = getUser(principal);
        List<ProductFavorite> favorites = favoriteRepository.findByUser(user);

        List<ProductResponse> responseList = new ArrayList<>();
        for (ProductFavorite favorite : favorites) {
            ProductResponse response = toProductResponse(favorite.getProduct());
            responseList.add(response);
        }

        return responseList;
    }*/

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
    }

    private ProductResponse toProductResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSeller().getId(),
                product.getSeller().getDisplayName(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getCondition(),
                product.getParish(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}




    /*

    // TO IMPLEMENT (FIRST THOUGHTS)
    // Set Swipe Product by User and Product
    // Mod Swipe Product by User and Product
    // Get all Swipes by User
    // Get specific Swipe by User and Product

    // Add favorite by User and Product
    // Remove favorite by User and Product
    // Get all favorites by User
    // Get specific Favorite by User and Product

     */


