CREATE TABLE insurance_product (
                                   id UUID PRIMARY KEY,
                                   name VARCHAR(255) NOT NULL,
                                   category VARCHAR(50) NOT NULL,
                                   base_price DECIMAL(19, 2) NOT NULL,
                                   tariffed_price DECIMAL(19, 2) NOT NULL
);