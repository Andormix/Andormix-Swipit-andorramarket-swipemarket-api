package com.andormix.swipemarketapi.offer;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OfferRequest(

        @NotNull
        @Positive
        BigDecimal offeredPrice

) {
}

/*
*   id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    product_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    offered_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT current_timestamp,

 */