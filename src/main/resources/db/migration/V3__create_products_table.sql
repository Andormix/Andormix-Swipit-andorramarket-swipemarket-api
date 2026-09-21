CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    seller_id BIGINT NOT NULL,
    title VARCHAR(120) NOT NULL,
    description VARCHAR(2000) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(30) NOT NULL,
    product_condition VARCHAR(30) NOT NULL,
    parish VARCHAR(50) NOT NULL, -- e.g. AVAILABLE, RESERVED, SOLD, ARCHIVED, DELETED RGPD?
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_products_seller
      FOREIGN KEY (seller_id)
          REFERENCES app_users(id),

    CONSTRAINT chk_products_price
      CHECK (price >= 0)
);

CREATE INDEX idx_products_seller_id
    ON products(seller_id);

CREATE INDEX idx_products_status
    ON products(status);

CREATE INDEX idx_products_category
    ON products(category);