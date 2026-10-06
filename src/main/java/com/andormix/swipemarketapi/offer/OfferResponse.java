package com.andormix.swipemarketapi.offer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

public record OfferResponse(

        Long id,
        Long productId,
        String productTitle,
        Long buyerId,
        BigDecimal offeredPrice,
        OfferStatus status,
        Instant createdAt

) {
}

//id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
//product_id BIGINT NOT NULL,
//buyer_id BIGINT NOT NULL,
//offered_price DECIMAL(10,2) NOT NULL,
//status VARCHAR(30) DEFAULT 'PENDING',
//created_at TIMESTAMP DEFAULT current_timestamp,