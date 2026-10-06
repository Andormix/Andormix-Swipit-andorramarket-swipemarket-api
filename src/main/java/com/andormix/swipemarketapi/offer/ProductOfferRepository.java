package com.andormix.swipemarketapi.offer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOfferRepository extends JpaRepository<ProductOffer, Long> {

    List<ProductOffer> findAllByProductId(Long productId);
    boolean existsByBuyerIdAndProductId(Long buyerId, Long productId);
}
