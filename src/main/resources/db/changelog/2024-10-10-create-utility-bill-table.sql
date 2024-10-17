--liquibase formatter sql

--changeset Nika Avalishvili:1
CREATE TABLE utility_bill (
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    amount NUMERIC,
    subscription_provider_id BIGINT,
    customer_id BIGINT,
    is_paid BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_subscription_provider
      FOREIGN KEY (subscription_provider_id) REFERENCES subscription_provider(id),
    CONSTRAINT fk_customer
      FOREIGN KEY (customer_id) REFERENCES customer(id)
);