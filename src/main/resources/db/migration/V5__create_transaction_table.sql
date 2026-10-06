
CREATE TABLE transactions(

    id bigint primary key auto_increment,
    user_id bigint not null,
    product_id bigint not null,
    transaction_type varchar(30) not null,
    transaction_status varchar(30) default 'COMPLETED',
    amount decimal(10,2) not null,              -- Corrección de: double not null,
    created_At timestamp DEFAULT CURRENT_TIMESTAMP,              -- Corrección:He añadido current timestamp
    note TEXT,

    CONSTRAINT fk_transaction_user_id
        FOREIGN KEY (user_id)
            REFERENCES app_users(id),

    CONSTRAINT fk_transaction_product_id
        FOREIGN KEY (product_id)
            REFERENCES products(id)
);

-- Corregido migración flyweight a __ BUSCADO ./mvnw spring-boot:run