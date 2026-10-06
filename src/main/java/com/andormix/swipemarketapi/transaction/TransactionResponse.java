package com.andormix.swipemarketapi.transaction;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

public record TransactionResponse(

    Long id,
    Long userId,
    Long productId,
    TransactionType transactionType,
    TransactionStatus transactionStatus,
    BigDecimal amount,
    Instant createdAt,
    String note

          /*
        *    id bigint primary key auto_increment,
            user_id bigint not null,
            product_id bigint not null,
            transaction_type varchar(30) not null,
            transaction_status varchar(30) default 'COMPLETED',
            amount decimal(10,2) not null,              -- Corrección de: double not null,
            created_At timestamp DEFAULT CURRENT_TIMESTAMP,              -- Corrección:He añadido current timestamp
            note TEXT,
        *
        * */

){
}
