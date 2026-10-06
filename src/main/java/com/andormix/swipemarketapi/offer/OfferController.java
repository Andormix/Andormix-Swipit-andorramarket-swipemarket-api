package com.andormix.swipemarketapi.offer;

import com.andormix.swipemarketapi.security.AppUserPrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/products/{productId}/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long productId,
            @Valid @RequestBody OfferRequest offerRequest
            )
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(offerService.create(principal, offerRequest, productId));
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> viewOffers(
            @AuthenticationPrincipal AppUserPrincipal principal,
            @PathVariable Long productId
    )
    {
        return ResponseEntity.status(HttpStatus.OK).body(offerService.getOffersForProduct(principal, productId));
    }
}
