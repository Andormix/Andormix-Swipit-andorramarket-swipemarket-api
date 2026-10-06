package com.andormix.swipemarketapi.transaction;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record TransactionRequest(

        //@NotNull
        //Long userId, Usaremos sec context

        @NotNull
        Long productId,

        @NotNull
        TransactionType transactionType,

        @NotNull
        @Positive // @PositiveOrZero
        BigDecimal amount,

        //@NotBlank era opcional
        @Size(max = 255, message = "La nota no puede superar los 255 caracteres")
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

){};


