
CREATE TABLE reviews(

    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    product_id BIGINT NOT NULL ,
    user_id BIGINT NOT NULL,
    rating INT NOT NULL,
    comment VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reviews_product_id
        FOREIGN KEY (product_id)
            REFERENCES products(id),

    CONSTRAINT fk_reviews_user_id
        FOREIGN KEY (user_id)
            REFERENCES app_users(id),

    CONSTRAINT uk_product_id_user_id
        UNIQUE (product_id, user_id)
);