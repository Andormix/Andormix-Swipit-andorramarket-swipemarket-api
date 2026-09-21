CREATE TABLE application_info (
      id BIGINT AUTO_INCREMENT PRIMARY KEY,
      application_name VARCHAR(100) NOT NULL,
      created_at TIMESTAMP NOT NULL
);

INSERT INTO application_info (
    application_name,
    created_at
) VALUES (
     'swipe-market-api',
     CURRENT_TIMESTAMP
);