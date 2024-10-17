--liquibase formatter sql

--changeset Nika Avalishvili:1
CREATE TABLE subscription_provider (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(19, 2) DEFAULT 0.00,
    accumulated_income DECIMAL(19, 2) DEFAULT 0.00
);

--changeset Nika Avalishvili:2
CREATE TABLE customer (
    id BIGINT PRIMARY KEY,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255),
    phone VARCHAR(255)
);

--changeset Nika Avalishvili:3
CREATE TABLE customer_subscription (
   subscription_provider_id BIGINT NOT NULL,
   customer_id BIGINT NOT NULL,
   PRIMARY KEY (subscription_provider_id, customer_id),
   FOREIGN KEY (subscription_provider_id) REFERENCES subscription_provider(id),
   FOREIGN KEY (customer_id) REFERENCES customer(id)
);
