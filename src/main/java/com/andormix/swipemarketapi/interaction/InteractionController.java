package com.andormix.swipemarketapi.interaction;

import com.andormix.swipemarketapi.product.ProductCategory;
import com.andormix.swipemarketapi.product.ProductResponse;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class InteractionController {

    private final InteractionService interactionService;

    public InteractionController(
            InteractionService interactionService
    ) {
        this.interactionService = interactionService;
    }

    @PostMapping("/products/{productId}/swipe")
    public SwipeResponse swipe(
            @PathVariable Long productId,
            @Valid @RequestBody SwipeRequest request,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return interactionService.swipe(productId, request, principal);
    }

    @GetMapping("/swipes/interested")
    public List<ProductResponse> findInterestedProducts(
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return interactionService.findInterestedProducts(principal);
    }

    @PostMapping("/products/{productId}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFavorite(
            @PathVariable Long productId,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        interactionService.addFavorite(productId, principal);
    }

    @DeleteMapping("/products/{productId}/favorite")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(
            @PathVariable Long productId,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        interactionService.removeFavorite(productId, principal);
    }

    @GetMapping("/favorites")
    public List<ProductResponse> findFavorites(
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) String search,
            @AuthenticationPrincipal AppUserPrincipal principal
    ) {
        return interactionService.findFavorites(principal);
    }
}