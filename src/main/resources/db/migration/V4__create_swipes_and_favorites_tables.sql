CREATE TABLE product_swipes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL,  -- 'like', 'dislike', 'pass'
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_product_swipes_user
        FOREIGN KEY (user_id)
            REFERENCES app_users(id)
            ON DELETE CASCADE,

    CONSTRAINT fk_product_swipes_product
        FOREIGN KEY (product_id)
            REFERENCES products(id)
            ON DELETE CASCADE,

    CONSTRAINT uk_product_swipes_user_product  -- Només un per product/user
        UNIQUE (user_id, product_id)
);

CREATE INDEX idx_product_swipes_user_id
    ON product_swipes(user_id);

CREATE INDEX idx_product_swipes_action
    ON product_swipes(action);


CREATE TABLE product_favorites (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   user_id BIGINT NOT NULL,
   product_id BIGINT NOT NULL,
   created_at TIMESTAMP NOT NULL,

   CONSTRAINT fk_product_favorites_user
       FOREIGN KEY (user_id)
           REFERENCES app_users(id)
           ON DELETE CASCADE,

   CONSTRAINT fk_product_favorites_product
       FOREIGN KEY (product_id)
           REFERENCES products(id)
           ON DELETE CASCADE,

   CONSTRAINT uk_product_favorites_user_product  -- Només un per product/user
       UNIQUE (user_id, product_id)
);

CREATE INDEX idx_product_favorites_user_id
    ON product_favorites(user_id);

CREATE INDEX idx_product_favorites_product_id
    ON product_favorites(product_id);