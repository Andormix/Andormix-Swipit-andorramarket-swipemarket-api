CREATE TABLE product_offers(

    id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    product_id BIGINT NOT NULL,
    buyer_id BIGINT NOT NULL,
    offered_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT current_timestamp,

    CONSTRAINT uk_offers_product_id_buyer_id
       UNIQUE (product_id, buyer_id),

    CONSTRAINT fk_offers_product_id
       FOREIGN KEY (product_id)
       REFERENCES products(id),

    CONSTRAINT fk_offers_buyer_id
       FOREIGN KEY (buyer_id)
       REFERENCES app_users(id)
);