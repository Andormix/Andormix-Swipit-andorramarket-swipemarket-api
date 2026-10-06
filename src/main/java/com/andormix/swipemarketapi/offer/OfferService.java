package com.andormix.swipemarketapi.offer;

import com.andormix.swipemarketapi.product.Product;
import com.andormix.swipemarketapi.product.ProductRepository;
import com.andormix.swipemarketapi.security.AppUserPrincipal;
import com.andormix.swipemarketapi.user.User;
import com.andormix.swipemarketapi.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class OfferService {


    private final UserRepository userRepository;
    private final ProductOfferRepository productOfferRepository;
    private final ProductRepository productRepository;

    public OfferService(UserRepository userRepository, ProductOfferRepository productOfferRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.productOfferRepository = productOfferRepository;
        this.productRepository = productRepository;
    }

    public OfferResponse create(
            AppUserPrincipal principal,
            OfferRequest request,
            Long productId)
    {


        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResponseStatusException(

                        HttpStatus.NOT_FOUND,
                        "Product not Found"
                )
        );

        if (request.offeredPrice().compareTo(product.getPrice()) >= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Offer price must be lower than the current product price"
            );
        }

        if (productOfferRepository.existsByBuyerIdAndProductId(principal.getUserId(), productId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You have already made an offer for this product"
            );
        }

        User seller = product.getSeller();

        if(principal.getUserId().equals(seller.getId()))
        {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You cannot make offers to your own product"
            );

        }

        User buyer = userRepository.findById(principal.getUserId()).orElseThrow(
                () -> new ResponseStatusException(

                        HttpStatus.NOT_FOUND,
                        "User not Found"
                )
        );

        ProductOffer offer = new ProductOffer(

                product,
                buyer,
                request.offeredPrice()
        );

        return toOfferResponse(productOfferRepository.save(offer));
    }

    public List<OfferResponse> getOffersForProduct(
            AppUserPrincipal principal,
            Long productId)
    {

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found")
        );

        if (!product.getSeller().getId().equals(principal.getUserId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not authorized to view offers for this product"
            );
        }

        return productOfferRepository.findAllByProductId(productId).stream().map(this::toOfferResponse).toList();
    }



    // ------------------------ HELPERS -------------------------------------

    public OfferResponse toOfferResponse(ProductOffer productOffer)
    {
        return new OfferResponse(

                productOffer.getId(),
                productOffer.getProduct().getId(),
                productOffer.getProduct().getTitle(),
                productOffer.getBuyer().getId(),
                productOffer.getOfferedPrice(),
                productOffer.getStatus(),
                productOffer.getCreatedAt()

        );

    }

}
